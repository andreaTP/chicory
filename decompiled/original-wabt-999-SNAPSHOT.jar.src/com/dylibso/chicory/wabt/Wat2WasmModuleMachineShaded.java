/*     */ package com.dylibso.chicory.wabt;
/*     */ 
/*     */ import com.dylibso.chicory.runtime.ChicoryInterruptedException;
/*     */ import com.dylibso.chicory.runtime.ImportFunction;
/*     */ import com.dylibso.chicory.runtime.Instance;
/*     */ import com.dylibso.chicory.runtime.MemCopyWorkaround;
/*     */ import com.dylibso.chicory.runtime.Memory;
/*     */ import com.dylibso.chicory.runtime.OpcodeImpl;
/*     */ import com.dylibso.chicory.runtime.TagInstance;
/*     */ import com.dylibso.chicory.runtime.TrapException;
/*     */ import com.dylibso.chicory.runtime.WasmException;
/*     */ import com.dylibso.chicory.runtime.WasmRuntimeException;
/*     */ import com.dylibso.chicory.wasm.ChicoryException;
/*     */ import com.dylibso.chicory.wasm.InvalidException;
/*     */ import com.dylibso.chicory.wasm.types.FunctionType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Wat2WasmModuleMachineShaded
/*     */ {
/*     */   private static final boolean memCopyWorkaround;
/*     */   
/*     */   public static long[] callIndirect(long[] args, int typeId, int funcId, Instance instance) {
/*  26 */     FunctionType expectedType = instance.type(typeId);
/*  27 */     FunctionType actualType = instance.type(instance.functionType(funcId));
/*  28 */     if (!actualType.typesMatch(expectedType)) {
/*  29 */       throw throwIndirectCallTypeMismatch();
/*     */     }
/*  31 */     return instance.getMachine().call(funcId, args);
/*     */   }
/*     */   
/*     */   public static long[] callIndirect(long[] args, int funcId, Instance instance) {
/*  35 */     return instance.getMachine().call(funcId, args);
/*     */   }
/*     */   
/*     */   public static long[] callHostFunction(Instance instance, int funcId, long[] args) {
/*  39 */     ImportFunction imprt = instance.imports().function(funcId);
/*  40 */     return imprt.handle().apply(instance, args);
/*     */   }
/*     */   
/*     */   public static boolean isRefNull(int ref) {
/*  44 */     return (ref == -1);
/*     */   }
/*     */   
/*     */   public static int tableGet(int index, int tableIndex, Instance instance) {
/*  48 */     return OpcodeImpl.TABLE_GET(instance, tableIndex, index);
/*     */   }
/*     */   
/*     */   public static void tableSet(int index, int value, int tableIndex, Instance instance) {
/*  52 */     instance.table(tableIndex).setRef(index, value, instance);
/*     */   }
/*     */   
/*     */   public static int tableGrow(int value, int size, int tableIndex, Instance instance) {
/*  56 */     return instance.table(tableIndex).grow(size, value, instance);
/*     */   }
/*     */   
/*     */   public static int tableSize(int tableIndex, Instance instance) {
/*  60 */     return instance.table(tableIndex).size();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void tableFill(int offset, int value, int size, int tableIndex, Instance instance) {
/*  65 */     OpcodeImpl.TABLE_FILL(instance, tableIndex, size, value, offset);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void tableCopy(int d, int s, int size, int dstTableIndex, int srcTableIndex, Instance instance) {
/*  70 */     OpcodeImpl.TABLE_COPY(instance, srcTableIndex, dstTableIndex, size, s, d);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void tableInit(int offset, int elemidx, int size, int elementidx, int tableidx, Instance instance) {
/*  75 */     OpcodeImpl.TABLE_INIT(instance, tableidx, elementidx, size, elemidx, offset);
/*     */   }
/*     */   
/*     */   public static int i32_ge_u(int a, int b) {
/*  79 */     if (memCopyWorkaround)
/*     */     {
/*  81 */       return MemCopyWorkaround.i32_ge_u(a, b);
/*     */     }
/*     */     
/*  84 */     return OpcodeImpl.I32_GE_U(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  91 */     String prop = System.getProperty("chicory.memCopyWorkaround");
/*     */     
/*  93 */     if (prop != null) {
/*  94 */       memCopyWorkaround = Boolean.valueOf(prop).booleanValue();
/*     */     } else {
/*  96 */       memCopyWorkaround = MemCopyWorkaround.shouldUseMemWorkaround();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void memoryCopy(int destination, int offset, int size, Memory memory) {
/* 101 */     if (memCopyWorkaround) {
/*     */       
/* 103 */       MemCopyWorkaround.memoryCopy(destination, offset, size, memory);
/*     */     } else {
/*     */       
/* 106 */       memory.copy(destination, offset, size);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void memoryFill(int offset, byte value, int size, Memory memory) {
/* 111 */     int end = size + offset;
/* 112 */     memory.fill(value, offset, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void memoryInit(int destination, int offset, int size, int segmentId, Memory memory) {
/* 117 */     memory.initPassiveSegment(segmentId, destination, offset, size);
/*     */   }
/*     */   
/*     */   public static int memoryGrow(int size, Memory memory) {
/* 121 */     return memory.grow(size);
/*     */   }
/*     */   
/*     */   public static void memoryDrop(int segment, Memory memory) {
/* 125 */     memory.drop(segment);
/*     */   }
/*     */   
/*     */   public static int memoryPages(Memory memory) {
/* 129 */     return memory.pages();
/*     */   }
/*     */   
/*     */   public static byte memoryReadByte(int base, int offset, Memory memory) {
/* 133 */     return memory.read(getAddr(base, offset));
/*     */   }
/*     */   
/*     */   public static short memoryReadShort(int base, int offset, Memory memory) {
/* 137 */     return memory.readShort(getAddr(base, offset));
/*     */   }
/*     */   
/*     */   public static int memoryReadInt(int base, int offset, Memory memory) {
/* 141 */     return memory.readInt(getAddr(base, offset));
/*     */   }
/*     */   
/*     */   public static long memoryReadLong(int base, int offset, Memory memory) {
/* 145 */     return memory.readLong(getAddr(base, offset));
/*     */   }
/*     */   
/*     */   public static float memoryReadFloat(int base, int offset, Memory memory) {
/* 149 */     return memory.readFloat(getAddr(base, offset));
/*     */   }
/*     */   
/*     */   public static double memoryReadDouble(int base, int offset, Memory memory) {
/* 153 */     return memory.readDouble(getAddr(base, offset));
/*     */   }
/*     */   
/*     */   public static void memoryWriteByte(int base, byte value, int offset, Memory memory) {
/* 157 */     memory.writeByte(getAddr(base, offset), value);
/*     */   }
/*     */   
/*     */   public static void memoryWriteShort(int base, short value, int offset, Memory memory) {
/* 161 */     memory.writeShort(getAddr(base, offset), value);
/*     */   }
/*     */   
/*     */   public static void memoryWriteInt(int base, int value, int offset, Memory memory) {
/* 165 */     memory.writeI32(getAddr(base, offset), value);
/*     */   }
/*     */   
/*     */   public static void memoryWriteLong(int base, long value, int offset, Memory memory) {
/* 169 */     memory.writeLong(getAddr(base, offset), value);
/*     */   }
/*     */   
/*     */   public static void memoryWriteFloat(int base, float value, int offset, Memory memory) {
/* 173 */     memory.writeF32(getAddr(base, offset), value);
/*     */   }
/*     */   
/*     */   public static void memoryWriteDouble(int base, double value, int offset, Memory memory) {
/* 177 */     memory.writeF64(getAddr(base, offset), value);
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntByteRead(int base, int offset, Memory memory) {
/* 181 */     int ptr = getAddr(base, offset);
/* 182 */     return Byte.toUnsignedInt(memory.atomicReadByte(ptr));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntShortRead(int base, int offset, Memory memory) {
/* 186 */     int ptr = getAddr(base, offset);
/* 187 */     if (ptr % 2 != 0) {
/* 188 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 190 */     return Short.toUnsignedInt(memory.atomicReadShort(ptr));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRead(int base, int offset, Memory memory) {
/* 194 */     int ptr = getAddr(base, offset);
/* 195 */     if (ptr % 4 != 0) {
/* 196 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 198 */     return memory.atomicReadInt(ptr);
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRead(int base, int offset, Memory memory) {
/* 202 */     int ptr = getAddr(base, offset);
/* 203 */     if (ptr % 8 != 0) {
/* 204 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 206 */     return memory.atomicReadLong(ptr);
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongByteRead(int base, int offset, Memory memory) {
/* 210 */     int ptr = getAddr(base, offset);
/* 211 */     return Byte.toUnsignedLong(memory.atomicReadByte(ptr));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongShortRead(int base, int offset, Memory memory) {
/* 215 */     int ptr = getAddr(base, offset);
/* 216 */     if (ptr % 2 != 0) {
/* 217 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 219 */     return Short.toUnsignedLong(memory.atomicReadShort(ptr));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongIntRead(int base, int offset, Memory memory) {
/* 223 */     int ptr = getAddr(base, offset);
/* 224 */     if (ptr % 4 != 0) {
/* 225 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 227 */     return Integer.toUnsignedLong(memory.atomicReadInt(ptr));
/*     */   }
/*     */   
/*     */   public static void memoryAtomicIntWrite(int base, int value, int offset, Memory memory) {
/* 231 */     int ptr = getAddr(base, offset);
/* 232 */     if (ptr % 4 != 0) {
/* 233 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 235 */     memory.atomicWriteInt(ptr, value);
/*     */   }
/*     */   
/*     */   public static void memoryAtomicIntByteWrite(int base, byte value, int offset, Memory memory) {
/* 239 */     int ptr = getAddr(base, offset);
/* 240 */     memory.atomicWriteByte(ptr, value);
/*     */   }
/*     */   
/*     */   public static void memoryAtomicIntShortWrite(int base, short value, int offset, Memory memory) {
/* 244 */     int ptr = getAddr(base, offset);
/* 245 */     if (ptr % 2 != 0) {
/* 246 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 248 */     memory.atomicWriteShort(ptr, value);
/*     */   }
/*     */   
/*     */   public static void memoryAtomicLongWrite(int base, long value, int offset, Memory memory) {
/* 252 */     int ptr = getAddr(base, offset);
/* 253 */     if (ptr % 8 != 0) {
/* 254 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 256 */     memory.atomicWriteLong(ptr, value);
/*     */   }
/*     */   
/*     */   public static void memoryAtomicLongByteWrite(int base, byte value, int offset, Memory memory) {
/* 260 */     int ptr = getAddr(base, offset);
/* 261 */     memory.atomicWriteByte(ptr, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void memoryAtomicLongShortWrite(int base, short value, int offset, Memory memory) {
/* 266 */     int ptr = getAddr(base, offset);
/* 267 */     if (ptr % 2 != 0) {
/* 268 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 270 */     memory.atomicWriteShort(ptr, value);
/*     */   }
/*     */   
/*     */   public static void memoryAtomicLongIntWrite(int base, int value, int offset, Memory memory) {
/* 274 */     int ptr = getAddr(base, offset);
/* 275 */     if (ptr % 4 != 0) {
/* 276 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 278 */     memory.atomicWriteInt(ptr, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAddr(int base, int offset) {
/* 283 */     return (base < 0) ? base : (base + offset);
/*     */   }
/*     */   
/*     */   public static RuntimeException throwCallStackExhausted(StackOverflowError e) {
/* 287 */     throw new ChicoryException("call stack exhausted", e);
/*     */   }
/*     */   
/*     */   public static RuntimeException throwIndirectCallTypeMismatch() {
/* 291 */     return (RuntimeException)new ChicoryException("indirect call type mismatch");
/*     */   }
/*     */   
/*     */   public static RuntimeException throwOutOfBoundsMemoryAccess() {
/* 295 */     throw new WasmRuntimeException("out of bounds memory access");
/*     */   }
/*     */   
/*     */   public static RuntimeException throwTrapException() {
/* 299 */     throw new TrapException("Trapped on unreachable instruction");
/*     */   }
/*     */   
/*     */   public static RuntimeException throwUnknownFunction(int index) {
/* 303 */     throw new InvalidException(String.format("unknown function %d", new Object[] { Integer.valueOf(index) }));
/*     */   }
/*     */   
/*     */   public static void checkInterruption() {
/* 307 */     if (Thread.currentThread().isInterrupted()) {
/* 308 */       throw new ChicoryInterruptedException("Thread interrupted");
/*     */     }
/*     */   }
/*     */   
/*     */   public static long readGlobal(int index, Instance instance) {
/* 313 */     return instance.global(index).getValue();
/*     */   }
/*     */   
/*     */   public static void writeGlobal(long value, int index, Instance instance) {
/* 317 */     instance.global(index).setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WasmException createWasmException(long[] args, int tagNumber, Instance instance) {
/* 324 */     if (args == null) {
/* 325 */       args = new long[0];
/*     */     }
/* 327 */     WasmException e = new WasmException(instance, tagNumber, args);
/* 328 */     instance.registerException(e);
/* 329 */     return e;
/*     */   }
/*     */   
/*     */   public static boolean exceptionMatches(WasmException exception, int tag, Instance instance) {
/* 333 */     if (exception.instance() == instance && exception.tagIdx() == tag) {
/* 334 */       return true;
/*     */     }
/*     */     
/* 337 */     TagInstance currentCatchTag = instance.tag(tag);
/* 338 */     TagInstance exceptionTag = exception.instance().tag(exception.tagIdx());
/* 339 */     return (tag < instance.imports().tagCount() && currentCatchTag
/* 340 */       .type().typesMatch(exceptionTag.type()) && currentCatchTag
/* 341 */       .type().returnsMatch(exceptionTag.type()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int memoryAtomicIntRmwAdd(int base, int value, int offset, Memory memory) {
/* 346 */     int ptr = getAddr(base, offset);
/* 347 */     if (ptr % 4 != 0) {
/* 348 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 350 */     return memory.atomicAddInt(ptr, value);
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmwSub(int base, int value, int offset, Memory memory) {
/* 354 */     int ptr = getAddr(base, offset);
/* 355 */     if (ptr % 4 != 0) {
/* 356 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 358 */     return memory.atomicAddInt(ptr, -value);
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmwAnd(int base, int value, int offset, Memory memory) {
/* 362 */     int ptr = getAddr(base, offset);
/* 363 */     if (ptr % 4 != 0) {
/* 364 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 366 */     return memory.atomicAndInt(ptr, value);
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmwOr(int base, int value, int offset, Memory memory) {
/* 370 */     int ptr = getAddr(base, offset);
/* 371 */     if (ptr % 4 != 0) {
/* 372 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 374 */     return memory.atomicOrInt(ptr, value);
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmwXor(int base, int value, int offset, Memory memory) {
/* 378 */     int ptr = getAddr(base, offset);
/* 379 */     if (ptr % 4 != 0) {
/* 380 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 382 */     return memory.atomicXorInt(ptr, value);
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmwXchg(int base, int value, int offset, Memory memory) {
/* 386 */     int ptr = getAddr(base, offset);
/* 387 */     if (ptr % 4 != 0) {
/* 388 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 390 */     return memory.atomicXchgInt(ptr, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int memoryAtomicIntRmwCmpxchg(int base, int expected, int replacement, int offset, Memory memory) {
/* 395 */     int ptr = getAddr(base, offset);
/* 396 */     if (ptr % 4 != 0) {
/* 397 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 399 */     return memory.atomicCmpxchgInt(ptr, expected, replacement);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int memoryAtomicIntRmw8AddU(int base, int value, int offset, Memory memory) {
/* 404 */     int ptr = getAddr(base, offset);
/* 405 */     return Byte.toUnsignedInt(memory.atomicAddByte(ptr, (byte)value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw8SubU(int base, int value, int offset, Memory memory) {
/* 409 */     int ptr = getAddr(base, offset);
/* 410 */     return Byte.toUnsignedInt(memory.atomicAddByte(ptr, (byte)-value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw8AndU(int base, int value, int offset, Memory memory) {
/* 414 */     int ptr = getAddr(base, offset);
/* 415 */     return Byte.toUnsignedInt(memory.atomicAndByte(ptr, (byte)value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw8OrU(int base, int value, int offset, Memory memory) {
/* 419 */     int ptr = getAddr(base, offset);
/* 420 */     return Byte.toUnsignedInt(memory.atomicOrByte(ptr, (byte)value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw8XorU(int base, int value, int offset, Memory memory) {
/* 424 */     int ptr = getAddr(base, offset);
/* 425 */     return Byte.toUnsignedInt(memory.atomicXorByte(ptr, (byte)value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw8XchgU(int base, int value, int offset, Memory memory) {
/* 429 */     int ptr = getAddr(base, offset);
/* 430 */     return Byte.toUnsignedInt(memory.atomicXchgByte(ptr, (byte)value));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int memoryAtomicIntRmw8CmpxchgU(int base, int expected, int replacement, int offset, Memory memory) {
/* 435 */     int ptr = getAddr(base, offset);
/* 436 */     return Byte.toUnsignedInt(memory
/* 437 */         .atomicCmpxchgByte(ptr, (byte)expected, (byte)replacement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int memoryAtomicIntRmw16AddU(int base, int value, int offset, Memory memory) {
/* 442 */     int ptr = getAddr(base, offset);
/* 443 */     if (ptr % 2 != 0) {
/* 444 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 446 */     return memory.atomicAddShort(ptr, (short)value) & 0xFFFF;
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw16SubU(int base, int value, int offset, Memory memory) {
/* 450 */     int ptr = getAddr(base, offset);
/* 451 */     if (ptr % 2 != 0) {
/* 452 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 454 */     return Short.toUnsignedInt(memory.atomicAddShort(ptr, (short)-value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw16AndU(int base, int value, int offset, Memory memory) {
/* 458 */     int ptr = getAddr(base, offset);
/* 459 */     if (ptr % 2 != 0) {
/* 460 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 462 */     return Short.toUnsignedInt(memory.atomicAndShort(ptr, (short)value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw16OrU(int base, int value, int offset, Memory memory) {
/* 466 */     int ptr = getAddr(base, offset);
/* 467 */     if (ptr % 2 != 0) {
/* 468 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 470 */     return Short.toUnsignedInt(memory.atomicOrShort(ptr, (short)value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw16XorU(int base, int value, int offset, Memory memory) {
/* 474 */     int ptr = getAddr(base, offset);
/* 475 */     if (ptr % 2 != 0) {
/* 476 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 478 */     return Short.toUnsignedInt(memory.atomicXorShort(ptr, (short)value));
/*     */   }
/*     */   
/*     */   public static int memoryAtomicIntRmw16XchgU(int base, int value, int offset, Memory memory) {
/* 482 */     int ptr = getAddr(base, offset);
/* 483 */     if (ptr % 2 != 0) {
/* 484 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 486 */     return Short.toUnsignedInt(memory.atomicXchgShort(ptr, (short)value));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int memoryAtomicIntRmw16CmpxchgU(int base, int expected, int replacement, int offset, Memory memory) {
/* 491 */     int ptr = getAddr(base, offset);
/* 492 */     if (ptr % 2 != 0) {
/* 493 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 495 */     return Short.toUnsignedInt(memory
/* 496 */         .atomicCmpxchgShort(ptr, (short)expected, (short)replacement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long memoryAtomicLongRmw8AddU(int base, long value, int offset, Memory memory) {
/* 501 */     int ptr = getAddr(base, offset);
/* 502 */     return Byte.toUnsignedLong(memory.atomicAddByte(ptr, (byte)(int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw8SubU(int base, long value, int offset, Memory memory) {
/* 506 */     int ptr = getAddr(base, offset);
/* 507 */     return Byte.toUnsignedLong(memory.atomicAddByte(ptr, (byte)(int)-value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw8AndU(int base, long value, int offset, Memory memory) {
/* 511 */     int ptr = getAddr(base, offset);
/* 512 */     return Byte.toUnsignedLong(memory.atomicAndByte(ptr, (byte)(int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw8OrU(int base, long value, int offset, Memory memory) {
/* 516 */     int ptr = getAddr(base, offset);
/* 517 */     return Byte.toUnsignedLong(memory.atomicOrByte(ptr, (byte)(int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw8XorU(int base, long value, int offset, Memory memory) {
/* 521 */     int ptr = getAddr(base, offset);
/* 522 */     return Byte.toUnsignedLong(memory.atomicXorByte(ptr, (byte)(int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw8XchgU(int base, long value, int offset, Memory memory) {
/* 526 */     int ptr = getAddr(base, offset);
/* 527 */     return Byte.toUnsignedLong(memory.atomicXchgByte(ptr, (byte)(int)value));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long memoryAtomicLongRmw8CmpxchgU(int base, long expected, long replacement, int offset, Memory memory) {
/* 532 */     int ptr = getAddr(base, offset);
/* 533 */     return Byte.toUnsignedLong(memory
/* 534 */         .atomicCmpxchgByte(ptr, (byte)(int)expected, (byte)(int)replacement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long memoryAtomicLongRmw16AddU(int base, long value, int offset, Memory memory) {
/* 539 */     int ptr = getAddr(base, offset);
/* 540 */     if (ptr % 2 != 0) {
/* 541 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 543 */     return Short.toUnsignedLong(memory.atomicAddShort(ptr, (short)(int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw16SubU(int base, long value, int offset, Memory memory) {
/* 547 */     int ptr = getAddr(base, offset);
/* 548 */     if (ptr % 2 != 0) {
/* 549 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 551 */     return Short.toUnsignedLong(memory.atomicAddShort(ptr, (short)(int)-value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw16AndU(int base, long value, int offset, Memory memory) {
/* 555 */     int ptr = getAddr(base, offset);
/* 556 */     if (ptr % 2 != 0) {
/* 557 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 559 */     return Short.toUnsignedLong(memory.atomicAndShort(ptr, (short)(int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw16OrU(int base, long value, int offset, Memory memory) {
/* 563 */     int ptr = getAddr(base, offset);
/* 564 */     if (ptr % 2 != 0) {
/* 565 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 567 */     return Short.toUnsignedLong(memory.atomicOrShort(ptr, (short)(int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw16XorU(int base, long value, int offset, Memory memory) {
/* 571 */     int ptr = getAddr(base, offset);
/* 572 */     if (ptr % 2 != 0) {
/* 573 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 575 */     return Short.toUnsignedLong(memory.atomicXorShort(ptr, (short)(int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw16XchgU(int base, long value, int offset, Memory memory) {
/* 579 */     int ptr = getAddr(base, offset);
/* 580 */     if (ptr % 2 != 0) {
/* 581 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 583 */     return Short.toUnsignedLong(memory.atomicXchgShort(ptr, (short)(int)value));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long memoryAtomicLongRmw16CmpxchgU(int base, long expected, long replacement, int offset, Memory memory) {
/* 588 */     int ptr = getAddr(base, offset);
/* 589 */     if (ptr % 2 != 0) {
/* 590 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 592 */     return Short.toUnsignedLong(memory
/* 593 */         .atomicCmpxchgShort(ptr, (short)(int)expected, (short)(int)replacement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long memoryAtomicLongRmw32AddU(int base, long value, int offset, Memory memory) {
/* 598 */     int ptr = getAddr(base, offset);
/* 599 */     if (ptr % 4 != 0) {
/* 600 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 602 */     return Integer.toUnsignedLong(memory.atomicAddInt(ptr, (int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw32SubU(int base, long value, int offset, Memory memory) {
/* 606 */     int ptr = getAddr(base, offset);
/* 607 */     if (ptr % 4 != 0) {
/* 608 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 610 */     return Integer.toUnsignedLong(memory.atomicAddInt(ptr, (int)-value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw32AndU(int base, long value, int offset, Memory memory) {
/* 614 */     int ptr = getAddr(base, offset);
/* 615 */     if (ptr % 4 != 0) {
/* 616 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 618 */     return Integer.toUnsignedLong(memory.atomicAndInt(ptr, (int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw32OrU(int base, long value, int offset, Memory memory) {
/* 622 */     int ptr = getAddr(base, offset);
/* 623 */     if (ptr % 4 != 0) {
/* 624 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 626 */     return Integer.toUnsignedLong(memory.atomicOrInt(ptr, (int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw32XorU(int base, long value, int offset, Memory memory) {
/* 630 */     int ptr = getAddr(base, offset);
/* 631 */     if (ptr % 4 != 0) {
/* 632 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 634 */     return Integer.toUnsignedLong(memory.atomicXorInt(ptr, (int)value));
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmw32XchgU(int base, long value, int offset, Memory memory) {
/* 638 */     int ptr = getAddr(base, offset);
/* 639 */     if (ptr % 4 != 0) {
/* 640 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 642 */     return Integer.toUnsignedLong(memory.atomicXchgInt(ptr, (int)value));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long memoryAtomicLongRmw32CmpxchgU(int base, long expected, long replacement, int offset, Memory memory) {
/* 647 */     int ptr = getAddr(base, offset);
/* 648 */     if (ptr % 4 != 0) {
/* 649 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 651 */     return Integer.toUnsignedLong(memory
/* 652 */         .atomicCmpxchgInt(ptr, (int)expected, (int)replacement));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long memoryAtomicLongRmwAdd(int base, long value, int offset, Memory memory) {
/* 657 */     int ptr = getAddr(base, offset);
/* 658 */     if (ptr % 8 != 0) {
/* 659 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 661 */     return memory.atomicAddLong(ptr, value);
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmwSub(int base, long value, int offset, Memory memory) {
/* 665 */     int ptr = getAddr(base, offset);
/* 666 */     if (ptr % 8 != 0) {
/* 667 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 669 */     return memory.atomicAddLong(ptr, -value);
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmwAnd(int base, long value, int offset, Memory memory) {
/* 673 */     int ptr = getAddr(base, offset);
/* 674 */     if (ptr % 8 != 0) {
/* 675 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 677 */     return memory.atomicAndLong(ptr, value);
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmwOr(int base, long value, int offset, Memory memory) {
/* 681 */     int ptr = getAddr(base, offset);
/* 682 */     if (ptr % 8 != 0) {
/* 683 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 685 */     return memory.atomicOrLong(ptr, value);
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmwXor(int base, long value, int offset, Memory memory) {
/* 689 */     int ptr = getAddr(base, offset);
/* 690 */     if (ptr % 8 != 0) {
/* 691 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 693 */     return memory.atomicXorLong(ptr, value);
/*     */   }
/*     */   
/*     */   public static long memoryAtomicLongRmwXchg(int base, long value, int offset, Memory memory) {
/* 697 */     int ptr = getAddr(base, offset);
/* 698 */     if (ptr % 8 != 0) {
/* 699 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 701 */     return memory.atomicXchgLong(ptr, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long memoryAtomicLongRmwCmpxchg(int base, long expected, long replacement, int offset, Memory memory) {
/* 706 */     int ptr = getAddr(base, offset);
/* 707 */     if (ptr % 8 != 0) {
/* 708 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 710 */     return memory.atomicCmpxchgLong(ptr, expected, replacement);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int memoryAtomicWait32(int base, int expected, long timeout, int offset, Memory memory) {
/* 716 */     int ptr = getAddr(base, offset);
/* 717 */     if (ptr % 4 != 0) {
/* 718 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 720 */     return memory.atomicWait(ptr, expected, timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int memoryAtomicWait64(int base, long expected, long timeout, int offset, Memory memory) {
/* 725 */     int ptr = getAddr(base, offset);
/* 726 */     if (ptr % 8 != 0) {
/* 727 */       throw new InvalidException("unaligned atomic");
/*     */     }
/* 729 */     return memory.atomicWait(ptr, expected, timeout);
/*     */   }
/*     */   
/*     */   public static int memoryAtomicNotify(int base, int count, int offset, Memory memory) {
/* 733 */     int ptr = getAddr(base, offset);
/* 734 */     return memory.atomicNotify(ptr, count);
/*     */   }
/*     */   
/*     */   public static void memoryAtomicFence(Memory memory) {
/* 738 */     memory.atomicFence();
/*     */   }
/*     */ }


/* Location:              /home/andreatp/workspace/chicory6/wabt/target/original-wabt-999-SNAPSHOT.jar!/com/dylibso/chicory/wabt/Wat2WasmModuleMachineShaded.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */