package main

import (
	"strings"
	"unsafe"
)

// low level primitives to be used:

// alloc/free implementation from:
// https://github.com/tinygo-org/tinygo/blob/2a76ceb7dd5ea5a834ec470b724882564d9681b3/src/runtime/arch_tinygowasm_malloc.go#L7
var allocs = make(map[uintptr][]byte)

//export malloc
func libc_malloc(size uintptr) unsafe.Pointer {
	if size == 0 {
		return nil
	}
	buf := make([]byte, size)
	ptr := unsafe.Pointer(&buf[0])
	allocs[uintptr(ptr)] = buf
	return ptr
}

//export free
func libc_free(ptr unsafe.Pointer) {
	if ptr == nil {
		return
	}
	if _, ok := allocs[uintptr(ptr)]; ok {
		delete(allocs, uintptr(ptr))
	} else {
		panic("free: invalid pointer")
	}
}

// we decide to use C string format as it requires a single int pointer

func readCString(offset uint32) string {
	length := 0
	for {
		s := *(*int32)(unsafe.Pointer(uintptr(offset) + uintptr(length)))
		if byte(s) == 0 {
			break
		}
		length++
	}

	buffer := make([]byte, length)
	for i := 0; i < int(length); i++ {
		s := *(*int32)(unsafe.Pointer(uintptr(offset) + uintptr(i)))
		buffer[i] = byte(s)
	}
	return string(buffer)
}

// inspired by:
// https://github.com/tinygo-org/tinygo/blob/2a76ceb7dd5ea5a834ec470b724882564d9681b3/src/runtime/string.go#L278
func writeCString(offset uintptr, str string) {
	stringData := []byte(str)
	for i := 0; i < len(stringData); i++ {
		*(*byte)(unsafe.Pointer(uintptr(offset) + uintptr(i))) = stringData[i]
	}
	*(*byte)(unsafe.Pointer(uintptr(offset) + uintptr(len(stringData)))) = 0 // trailing 0 byte
}

// this is the "Debezium Guest SDK" implementation
// wrapping together the low level primitives and using the
// "Debezium Host SDK" functionality

//go:wasm-module env
//export struct_get_string
func envStructGetString(structPtr, fieldNamePtr uint32) uint32

func structGetString(structPtr uint32, fieldName string) string {
	var fieldNameLen = len(fieldName) + 1
	var fieldNamePtr = libc_malloc(uintptr(fieldNameLen))
	writeCString(uintptr(fieldNamePtr), fieldName)

	var resultPtr = envStructGetString(structPtr, uint32(uintptr(fieldNamePtr)))

	var result = readCString(resultPtr)
	libc_free(unsafe.Pointer(uintptr(resultPtr)))
	return result
}

//go:wasm-module env
//export struct_get_struct
func envStructGetStruct(structPtr, fieldNamePtr uint32) uint32

func structGetStruct(structPtr uint32, fieldName string) uint32 {
	var fieldNameLen = len(fieldName) + 1
	var fieldNamePtr = libc_malloc(uintptr(fieldNameLen))
	writeCString(uintptr(fieldNamePtr), fieldName)

	return envStructGetStruct(structPtr, uint32(uintptr(fieldNamePtr)))
}

// now that we have some low-level building blocks
// we can start building "higher level" abstractions

func getString(structPtr uint32, path string) string {
	split := strings.Split(path, `.`)
	currentPtr := structPtr
	for index, segment := range split {
		if index == len(split)-1 {
			return structGetString(currentPtr, segment)
		} else {
			currentPtr = structGetStruct(currentPtr, segment)
		}
	}
	return "not found"
}

func writeString(str string) uint32 {
	var strLen = len(str) + 1
	var strPtr = libc_malloc(uintptr(strLen))
	writeCString(uintptr(strPtr), str)
	return uint32(uintptr(strPtr))
}

// All of the above code should be packaged in an SDK.
// This is (more or less) the code the user will write:
//
//export process
func process(parentPtr uint32) uint32 {
	var result = getString(parentPtr, "child.name")

	return writeString(result)
}

func main() {}
