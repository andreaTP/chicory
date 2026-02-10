/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wabt;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.wabt.Wat2WasmModuleMachineFuncGroup_0;
import com.dylibso.chicory.wabt.Wat2WasmModuleMachineMachineCall;
import com.dylibso.chicory.wabt.Wat2WasmModuleMachineShaded;
import com.dylibso.chicory.wasm.types.Value;

public final class Wat2WasmModuleMachine
implements Machine {
    private final Instance instance;

    public Wat2WasmModuleMachine(Instance instance) {
        this.instance = instance;
    }

    @Override
    public long[] call(int n, long[] lArray) {
        try {
            Instance instance = this.instance;
            return Wat2WasmModuleMachineMachineCall.call(instance, instance.memory(), n, lArray);
        }
        catch (StackOverflowError stackOverflowError) {
            throw Wat2WasmModuleMachineShaded.throwCallStackExhausted(stackOverflowError);
        }
    }

    public static void call_indirect_0(int n, int n2, int n3, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n3);
        int n4 = tableInstance.requiredRef(n2);
        Instance instance2 = tableInstance.instance(n2);
        if (instance2 == null || instance2 == instance) {
            int n5 = n;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n4) {
                case 14: {
                    Wat2WasmModuleMachineFuncGroup_0.func_14(n5, memory2, instance3);
                    return;
                }
                case 19: {
                    Wat2WasmModuleMachineFuncGroup_0.func_19(n5, memory2, instance3);
                    return;
                }
                case 20: {
                    Wat2WasmModuleMachineFuncGroup_0.func_20(n5, memory2, instance3);
                    return;
                }
                case 22: {
                    Wat2WasmModuleMachineFuncGroup_0.func_22(n5, memory2, instance3);
                    return;
                }
                case 23: {
                    Wat2WasmModuleMachineFuncGroup_0.func_23(n5, memory2, instance3);
                    return;
                }
                case 24: {
                    Wat2WasmModuleMachineFuncGroup_0.func_24(n5, memory2, instance3);
                    return;
                }
                case 26: {
                    Wat2WasmModuleMachineFuncGroup_0.func_26(n5, memory2, instance3);
                    return;
                }
                case 27: {
                    Wat2WasmModuleMachineFuncGroup_0.func_27(n5, memory2, instance3);
                    return;
                }
                case 28: {
                    Wat2WasmModuleMachineFuncGroup_0.func_28(n5, memory2, instance3);
                    return;
                }
                case 29: {
                    Wat2WasmModuleMachineFuncGroup_0.func_29(n5, memory2, instance3);
                    return;
                }
                case 31: {
                    Wat2WasmModuleMachineFuncGroup_0.func_31(n5, memory2, instance3);
                    return;
                }
                case 90: {
                    Wat2WasmModuleMachineFuncGroup_0.func_90(n5, memory2, instance3);
                    return;
                }
                case 91: {
                    Wat2WasmModuleMachineFuncGroup_0.func_91(n5, memory2, instance3);
                    return;
                }
                case 92: {
                    Wat2WasmModuleMachineFuncGroup_0.func_92(n5, memory2, instance3);
                    return;
                }
                case 93: {
                    Wat2WasmModuleMachineFuncGroup_0.func_93(n5, memory2, instance3);
                    return;
                }
                case 94: {
                    Wat2WasmModuleMachineFuncGroup_0.func_94(n5, memory2, instance3);
                    return;
                }
                case 95: {
                    Wat2WasmModuleMachineFuncGroup_0.func_95(n5, memory2, instance3);
                    return;
                }
                case 96: {
                    Wat2WasmModuleMachineFuncGroup_0.func_96(n5, memory2, instance3);
                    return;
                }
                case 97: {
                    Wat2WasmModuleMachineFuncGroup_0.func_97(n5, memory2, instance3);
                    return;
                }
                case 98: {
                    Wat2WasmModuleMachineFuncGroup_0.func_98(n5, memory2, instance3);
                    return;
                }
                case 99: {
                    Wat2WasmModuleMachineFuncGroup_0.func_99(n5, memory2, instance3);
                    return;
                }
                case 100: {
                    Wat2WasmModuleMachineFuncGroup_0.func_100(n5, memory2, instance3);
                    return;
                }
                case 101: {
                    Wat2WasmModuleMachineFuncGroup_0.func_101(n5, memory2, instance3);
                    return;
                }
                case 102: {
                    Wat2WasmModuleMachineFuncGroup_0.func_102(n5, memory2, instance3);
                    return;
                }
                case 103: {
                    Wat2WasmModuleMachineFuncGroup_0.func_103(n5, memory2, instance3);
                    return;
                }
                case 104: {
                    Wat2WasmModuleMachineFuncGroup_0.func_104(n5, memory2, instance3);
                    return;
                }
                case 105: {
                    Wat2WasmModuleMachineFuncGroup_0.func_105(n5, memory2, instance3);
                    return;
                }
                case 106: {
                    Wat2WasmModuleMachineFuncGroup_0.func_106(n5, memory2, instance3);
                    return;
                }
                case 107: {
                    Wat2WasmModuleMachineFuncGroup_0.func_107(n5, memory2, instance3);
                    return;
                }
                case 108: {
                    Wat2WasmModuleMachineFuncGroup_0.func_108(n5, memory2, instance3);
                    return;
                }
                case 109: {
                    Wat2WasmModuleMachineFuncGroup_0.func_109(n5, memory2, instance3);
                    return;
                }
                case 200: {
                    Wat2WasmModuleMachineFuncGroup_0.func_200(n5, memory2, instance3);
                    return;
                }
                case 202: {
                    Wat2WasmModuleMachineFuncGroup_0.func_202(n5, memory2, instance3);
                    return;
                }
                case 213: {
                    Wat2WasmModuleMachineFuncGroup_0.func_213(n5, memory2, instance3);
                    return;
                }
                case 217: {
                    Wat2WasmModuleMachineFuncGroup_0.func_217(n5, memory2, instance3);
                    return;
                }
                case 219: {
                    Wat2WasmModuleMachineFuncGroup_0.func_219(n5, memory2, instance3);
                    return;
                }
                case 235: {
                    Wat2WasmModuleMachineFuncGroup_0.func_235(n5, memory2, instance3);
                    return;
                }
                case 236: {
                    Wat2WasmModuleMachineFuncGroup_0.func_236(n5, memory2, instance3);
                    return;
                }
                case 243: {
                    Wat2WasmModuleMachineFuncGroup_0.func_243(n5, memory2, instance3);
                    return;
                }
                case 244: {
                    Wat2WasmModuleMachineFuncGroup_0.func_244(n5, memory2, instance3);
                    return;
                }
                case 460: {
                    Wat2WasmModuleMachineFuncGroup_0.func_460(n5, memory2, instance3);
                    return;
                }
                case 799: {
                    Wat2WasmModuleMachineFuncGroup_0.func_799(n5, memory2, instance3);
                    return;
                }
                case 831: {
                    Wat2WasmModuleMachineFuncGroup_0.func_831(n5, memory2, instance3);
                    return;
                }
                case 832: {
                    Wat2WasmModuleMachineFuncGroup_0.func_832(n5, memory2, instance3);
                    return;
                }
                case 841: {
                    Wat2WasmModuleMachineFuncGroup_0.func_841(n5, memory2, instance3);
                    return;
                }
                case 1005: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1005(n5, memory2, instance3);
                    return;
                }
                case 1084: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1084(n5, memory2, instance3);
                    return;
                }
                case 1088: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1088(n5, memory2, instance3);
                    return;
                }
                case 1089: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1089(n5, memory2, instance3);
                    return;
                }
                case 1090: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1090(n5, memory2, instance3);
                    return;
                }
                case 1092: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1092(n5, memory2, instance3);
                    return;
                }
                case 1103: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1103(n5, memory2, instance3);
                    return;
                }
                case 1217: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1217(n5, memory2, instance3);
                    return;
                }
                case 1312: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1312(n5, memory2, instance3);
                    return;
                }
                case 1314: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1314(n5, memory2, instance3);
                    return;
                }
                case 1316: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1316(n5, memory2, instance3);
                    return;
                }
                case 1317: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1317(n5, memory2, instance3);
                    return;
                }
                case 1394: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1394(n5, memory2, instance3);
                    return;
                }
                case 1395: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1395(n5, memory2, instance3);
                    return;
                }
                case 1397: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1397(n5, memory2, instance3);
                    return;
                }
                case 1398: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1398(n5, memory2, instance3);
                    return;
                }
                case 1400: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1400(n5, memory2, instance3);
                    return;
                }
                case 1402: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1402(n5, memory2, instance3);
                    return;
                }
                case 1403: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1403(n5, memory2, instance3);
                    return;
                }
                case 1405: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1405(n5, memory2, instance3);
                    return;
                }
                case 1406: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1406(n5, memory2, instance3);
                    return;
                }
                case 1411: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1411(n5, memory2, instance3);
                    return;
                }
                case 1415: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1415(n5, memory2, instance3);
                    return;
                }
                case 1417: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1417(n5, memory2, instance3);
                    return;
                }
                case 1419: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1419(n5, memory2, instance3);
                    return;
                }
                case 1421: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1421(n5, memory2, instance3);
                    return;
                }
                case 1422: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1422(n5, memory2, instance3);
                    return;
                }
                case 1424: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1424(n5, memory2, instance3);
                    return;
                }
                case 1426: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1426(n5, memory2, instance3);
                    return;
                }
                case 1428: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1428(n5, memory2, instance3);
                    return;
                }
                case 1431: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1431(n5, memory2, instance3);
                    return;
                }
                case 1433: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1433(n5, memory2, instance3);
                    return;
                }
                case 1435: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1435(n5, memory2, instance3);
                    return;
                }
                case 1437: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1437(n5, memory2, instance3);
                    return;
                }
                case 1439: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1439(n5, memory2, instance3);
                    return;
                }
                case 1441: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1441(n5, memory2, instance3);
                    return;
                }
                case 1443: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1443(n5, memory2, instance3);
                    return;
                }
                case 1446: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1446(n5, memory2, instance3);
                    return;
                }
                case 1448: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1448(n5, memory2, instance3);
                    return;
                }
                case 1450: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1450(n5, memory2, instance3);
                    return;
                }
                case 1452: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1452(n5, memory2, instance3);
                    return;
                }
                case 1454: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1454(n5, memory2, instance3);
                    return;
                }
                case 1455: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1455(n5, memory2, instance3);
                    return;
                }
                case 1456: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1456(n5, memory2, instance3);
                    return;
                }
                case 1457: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1457(n5, memory2, instance3);
                    return;
                }
                case 1459: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1459(n5, memory2, instance3);
                    return;
                }
                case 1461: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1461(n5, memory2, instance3);
                    return;
                }
                case 1463: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1463(n5, memory2, instance3);
                    return;
                }
                case 1464: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1464(n5, memory2, instance3);
                    return;
                }
                case 1466: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1466(n5, memory2, instance3);
                    return;
                }
                case 1468: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1468(n5, memory2, instance3);
                    return;
                }
                case 1470: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1470(n5, memory2, instance3);
                    return;
                }
                case 1472: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1472(n5, memory2, instance3);
                    return;
                }
                case 1474: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1474(n5, memory2, instance3);
                    return;
                }
                case 1476: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1476(n5, memory2, instance3);
                    return;
                }
                case 1478: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1478(n5, memory2, instance3);
                    return;
                }
                case 1480: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1480(n5, memory2, instance3);
                    return;
                }
                case 1482: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1482(n5, memory2, instance3);
                    return;
                }
                case 1483: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1483(n5, memory2, instance3);
                    return;
                }
                case 1485: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1485(n5, memory2, instance3);
                    return;
                }
                case 1486: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1486(n5, memory2, instance3);
                    return;
                }
                case 1488: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1488(n5, memory2, instance3);
                    return;
                }
                case 1489: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1489(n5, memory2, instance3);
                    return;
                }
                case 1490: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1490(n5, memory2, instance3);
                    return;
                }
                case 1491: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1491(n5, memory2, instance3);
                    return;
                }
                case 1492: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1492(n5, memory2, instance3);
                    return;
                }
                case 1494: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1494(n5, memory2, instance3);
                    return;
                }
                case 1496: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1496(n5, memory2, instance3);
                    return;
                }
                case 1498: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1498(n5, memory2, instance3);
                    return;
                }
                case 1500: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1500(n5, memory2, instance3);
                    return;
                }
                case 1502: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1502(n5, memory2, instance3);
                    return;
                }
                case 1504: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1504(n5, memory2, instance3);
                    return;
                }
                case 1506: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1506(n5, memory2, instance3);
                    return;
                }
                case 1508: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1508(n5, memory2, instance3);
                    return;
                }
                case 1510: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1510(n5, memory2, instance3);
                    return;
                }
                case 1512: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1512(n5, memory2, instance3);
                    return;
                }
                case 1514: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1514(n5, memory2, instance3);
                    return;
                }
                case 1516: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1516(n5, memory2, instance3);
                    return;
                }
                case 1518: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1518(n5, memory2, instance3);
                    return;
                }
                case 1519: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1519(n5, memory2, instance3);
                    return;
                }
                case 1520: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1520(n5, memory2, instance3);
                    return;
                }
                case 1522: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1522(n5, memory2, instance3);
                    return;
                }
                case 1523: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1523(n5, memory2, instance3);
                    return;
                }
                case 1525: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1525(n5, memory2, instance3);
                    return;
                }
                case 1527: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1527(n5, memory2, instance3);
                    return;
                }
                case 1528: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1528(n5, memory2, instance3);
                    return;
                }
                case 1530: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1530(n5, memory2, instance3);
                    return;
                }
                case 1531: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1531(n5, memory2, instance3);
                    return;
                }
                case 1533: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1533(n5, memory2, instance3);
                    return;
                }
                case 1534: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1534(n5, memory2, instance3);
                    return;
                }
                case 1536: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1536(n5, memory2, instance3);
                    return;
                }
                case 1537: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1537(n5, memory2, instance3);
                    return;
                }
                case 1539: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1539(n5, memory2, instance3);
                    return;
                }
                case 1540: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1540(n5, memory2, instance3);
                    return;
                }
                case 1542: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1542(n5, memory2, instance3);
                    return;
                }
                case 1543: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1543(n5, memory2, instance3);
                    return;
                }
                case 1544: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1544(n5, memory2, instance3);
                    return;
                }
                case 1546: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1546(n5, memory2, instance3);
                    return;
                }
                case 1547: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1547(n5, memory2, instance3);
                    return;
                }
                case 1549: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1549(n5, memory2, instance3);
                    return;
                }
                case 1550: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1550(n5, memory2, instance3);
                    return;
                }
                case 1552: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1552(n5, memory2, instance3);
                    return;
                }
                case 1554: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1554(n5, memory2, instance3);
                    return;
                }
                case 1556: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1556(n5, memory2, instance3);
                    return;
                }
                case 1558: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1558(n5, memory2, instance3);
                    return;
                }
                case 1560: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1560(n5, memory2, instance3);
                    return;
                }
                case 1562: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1562(n5, memory2, instance3);
                    return;
                }
                case 1564: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1564(n5, memory2, instance3);
                    return;
                }
                case 1567: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1567(n5, memory2, instance3);
                    return;
                }
                case 1570: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1570(n5, memory2, instance3);
                    return;
                }
                case 1574: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1574(n5, memory2, instance3);
                    return;
                }
                case 1576: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1576(n5, memory2, instance3);
                    return;
                }
                case 1591: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1591(n5, memory2, instance3);
                    return;
                }
                case 1602: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1602(n5, memory2, instance3);
                    return;
                }
                case 1612: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1612(n5, memory2, instance3);
                    return;
                }
                case 1618: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1618(n5, memory2, instance3);
                    return;
                }
                case 1632: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1632(n5, memory2, instance3);
                    return;
                }
                case 1655: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1655(n5, memory2, instance3);
                    return;
                }
                case 1712: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1712(n5, memory2, instance3);
                    return;
                }
                case 1742: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1742(n5, memory2, instance3);
                    return;
                }
                case 1743: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1743(n5, memory2, instance3);
                    return;
                }
                case 1745: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1745(n5, memory2, instance3);
                    return;
                }
                case 1747: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1747(n5, memory2, instance3);
                    return;
                }
                case 1749: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1749(n5, memory2, instance3);
                    return;
                }
                case 1751: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1751(n5, memory2, instance3);
                    return;
                }
                case 1777: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1777(n5, memory2, instance3);
                    return;
                }
                case 1781: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1781(n5, memory2, instance3);
                    return;
                }
                case 1784: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1784(n5, memory2, instance3);
                    return;
                }
                case 1785: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1785(n5, memory2, instance3);
                    return;
                }
                case 1790: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1790(n5, memory2, instance3);
                    return;
                }
                case 1806: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1806(n5, memory2, instance3);
                    return;
                }
                case 1827: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1827(n5, memory2, instance3);
                    return;
                }
                case 1830: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1830(n5, memory2, instance3);
                    return;
                }
                case 1862: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1862(n5, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n}, 0, n4, instance2);
    }

    public static int call_indirect_1(int n, int n2, int n3, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n3);
        int n4 = tableInstance.requiredRef(n2);
        Instance instance2 = tableInstance.instance(n2);
        if (instance2 == null || instance2 == instance) {
            int n5 = n;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n4) {
                case 4: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_4(n5, memory2, instance3);
                }
                case 32: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_32(n5, memory2, instance3);
                }
                case 41: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_41(n5, memory2, instance3);
                }
                case 78: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_78(n5, memory2, instance3);
                }
                case 79: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_79(n5, memory2, instance3);
                }
                case 80: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_80(n5, memory2, instance3);
                }
                case 130: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_130(n5, memory2, instance3);
                }
                case 154: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_154(n5, memory2, instance3);
                }
                case 162: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_162(n5, memory2, instance3);
                }
                case 167: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_167(n5, memory2, instance3);
                }
                case 186: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_186(n5, memory2, instance3);
                }
                case 195: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_195(n5, memory2, instance3);
                }
                case 216: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_216(n5, memory2, instance3);
                }
                case 218: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_218(n5, memory2, instance3);
                }
                case 228: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_228(n5, memory2, instance3);
                }
                case 230: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_230(n5, memory2, instance3);
                }
                case 234: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_234(n5, memory2, instance3);
                }
                case 242: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_242(n5, memory2, instance3);
                }
                case 273: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_273(n5, memory2, instance3);
                }
                case 275: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_275(n5, memory2, instance3);
                }
                case 286: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_286(n5, memory2, instance3);
                }
                case 287: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_287(n5, memory2, instance3);
                }
                case 289: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_289(n5, memory2, instance3);
                }
                case 314: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_314(n5, memory2, instance3);
                }
                case 317: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_317(n5, memory2, instance3);
                }
                case 318: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_318(n5, memory2, instance3);
                }
                case 331: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_331(n5, memory2, instance3);
                }
                case 332: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_332(n5, memory2, instance3);
                }
                case 334: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_334(n5, memory2, instance3);
                }
                case 364: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_364(n5, memory2, instance3);
                }
                case 369: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_369(n5, memory2, instance3);
                }
                case 455: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_455(n5, memory2, instance3);
                }
                case 456: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_456(n5, memory2, instance3);
                }
                case 458: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_458(n5, memory2, instance3);
                }
                case 538: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_538(n5, memory2, instance3);
                }
                case 568: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_568(n5, memory2, instance3);
                }
                case 642: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_642(n5, memory2, instance3);
                }
                case 643: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_643(n5, memory2, instance3);
                }
                case 646: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_646(n5, memory2, instance3);
                }
                case 649: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_649(n5, memory2, instance3);
                }
                case 653: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_653(n5, memory2, instance3);
                }
                case 656: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_656(n5, memory2, instance3);
                }
                case 659: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_659(n5, memory2, instance3);
                }
                case 665: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_665(n5, memory2, instance3);
                }
                case 668: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_668(n5, memory2, instance3);
                }
                case 671: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_671(n5, memory2, instance3);
                }
                case 676: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_676(n5, memory2, instance3);
                }
                case 687: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_687(n5, memory2, instance3);
                }
                case 689: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_689(n5, memory2, instance3);
                }
                case 693: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_693(n5, memory2, instance3);
                }
                case 694: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_694(n5, memory2, instance3);
                }
                case 695: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_695(n5, memory2, instance3);
                }
                case 718: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_718(n5, memory2, instance3);
                }
                case 719: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_719(n5, memory2, instance3);
                }
                case 723: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_723(n5, memory2, instance3);
                }
                case 728: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_728(n5, memory2, instance3);
                }
                case 729: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_729(n5, memory2, instance3);
                }
                case 734: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_734(n5, memory2, instance3);
                }
                case 743: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_743(n5, memory2, instance3);
                }
                case 750: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_750(n5, memory2, instance3);
                }
                case 753: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_753(n5, memory2, instance3);
                }
                case 759: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_759(n5, memory2, instance3);
                }
                case 761: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_761(n5, memory2, instance3);
                }
                case 766: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_766(n5, memory2, instance3);
                }
                case 769: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_769(n5, memory2, instance3);
                }
                case 775: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_775(n5, memory2, instance3);
                }
                case 777: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_777(n5, memory2, instance3);
                }
                case 781: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_781(n5, memory2, instance3);
                }
                case 784: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_784(n5, memory2, instance3);
                }
                case 786: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_786(n5, memory2, instance3);
                }
                case 800: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_800(n5, memory2, instance3);
                }
                case 839: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_839(n5, memory2, instance3);
                }
                case 840: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_840(n5, memory2, instance3);
                }
                case 846: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_846(n5, memory2, instance3);
                }
                case 849: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_849(n5, memory2, instance3);
                }
                case 857: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_857(n5, memory2, instance3);
                }
                case 867: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_867(n5, memory2, instance3);
                }
                case 871: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_871(n5, memory2, instance3);
                }
                case 875: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_875(n5, memory2, instance3);
                }
                case 879: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_879(n5, memory2, instance3);
                }
                case 887: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_887(n5, memory2, instance3);
                }
                case 891: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_891(n5, memory2, instance3);
                }
                case 894: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_894(n5, memory2, instance3);
                }
                case 901: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_901(n5, memory2, instance3);
                }
                case 903: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_903(n5, memory2, instance3);
                }
                case 932: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_932(n5, memory2, instance3);
                }
                case 935: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_935(n5, memory2, instance3);
                }
                case 939: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_939(n5, memory2, instance3);
                }
                case 940: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_940(n5, memory2, instance3);
                }
                case 941: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_941(n5, memory2, instance3);
                }
                case 971: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_971(n5, memory2, instance3);
                }
                case 972: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_972(n5, memory2, instance3);
                }
                case 974: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_974(n5, memory2, instance3);
                }
                case 978: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_978(n5, memory2, instance3);
                }
                case 981: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_981(n5, memory2, instance3);
                }
                case 987: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_987(n5, memory2, instance3);
                }
                case 989: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_989(n5, memory2, instance3);
                }
                case 1008: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1008(n5, memory2, instance3);
                }
                case 1016: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1016(n5, memory2, instance3);
                }
                case 1019: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1019(n5, memory2, instance3);
                }
                case 1038: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1038(n5, memory2, instance3);
                }
                case 1042: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1042(n5, memory2, instance3);
                }
                case 1051: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1051(n5, memory2, instance3);
                }
                case 1055: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1055(n5, memory2, instance3);
                }
                case 1059: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1059(n5, memory2, instance3);
                }
                case 1075: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1075(n5, memory2, instance3);
                }
                case 1079: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1079(n5, memory2, instance3);
                }
                case 1085: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1085(n5, memory2, instance3);
                }
                case 1087: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1087(n5, memory2, instance3);
                }
                case 1091: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1091(n5, memory2, instance3);
                }
                case 1102: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1102(n5, memory2, instance3);
                }
                case 1156: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1156(n5, memory2, instance3);
                }
                case 1157: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1157(n5, memory2, instance3);
                }
                case 1169: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1169(n5, memory2, instance3);
                }
                case 1211: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1211(n5, memory2, instance3);
                }
                case 1239: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1239(n5, memory2, instance3);
                }
                case 1241: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1241(n5, memory2, instance3);
                }
                case 1244: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1244(n5, memory2, instance3);
                }
                case 1245: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1245(n5, memory2, instance3);
                }
                case 1246: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1246(n5, memory2, instance3);
                }
                case 1247: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1247(n5, memory2, instance3);
                }
                case 1248: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1248(n5, memory2, instance3);
                }
                case 1250: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1250(n5, memory2, instance3);
                }
                case 1251: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1251(n5, memory2, instance3);
                }
                case 1253: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1253(n5, memory2, instance3);
                }
                case 1256: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1256(n5, memory2, instance3);
                }
                case 1262: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1262(n5, memory2, instance3);
                }
                case 1264: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1264(n5, memory2, instance3);
                }
                case 1266: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1266(n5, memory2, instance3);
                }
                case 1299: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1299(n5, memory2, instance3);
                }
                case 1305: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1305(n5, memory2, instance3);
                }
                case 1311: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1311(n5, memory2, instance3);
                }
                case 1313: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1313(n5, memory2, instance3);
                }
                case 1315: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1315(n5, memory2, instance3);
                }
                case 1392: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1392(n5, memory2, instance3);
                }
                case 1393: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1393(n5, memory2, instance3);
                }
                case 1396: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1396(n5, memory2, instance3);
                }
                case 1399: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1399(n5, memory2, instance3);
                }
                case 1401: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1401(n5, memory2, instance3);
                }
                case 1404: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1404(n5, memory2, instance3);
                }
                case 1410: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1410(n5, memory2, instance3);
                }
                case 1414: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1414(n5, memory2, instance3);
                }
                case 1416: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1416(n5, memory2, instance3);
                }
                case 1418: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1418(n5, memory2, instance3);
                }
                case 1420: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1420(n5, memory2, instance3);
                }
                case 1423: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1423(n5, memory2, instance3);
                }
                case 1425: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1425(n5, memory2, instance3);
                }
                case 1427: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1427(n5, memory2, instance3);
                }
                case 1429: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1429(n5, memory2, instance3);
                }
                case 1430: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1430(n5, memory2, instance3);
                }
                case 1432: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1432(n5, memory2, instance3);
                }
                case 1434: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1434(n5, memory2, instance3);
                }
                case 1436: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1436(n5, memory2, instance3);
                }
                case 1438: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1438(n5, memory2, instance3);
                }
                case 1440: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1440(n5, memory2, instance3);
                }
                case 1442: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1442(n5, memory2, instance3);
                }
                case 1444: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1444(n5, memory2, instance3);
                }
                case 1445: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1445(n5, memory2, instance3);
                }
                case 1447: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1447(n5, memory2, instance3);
                }
                case 1449: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1449(n5, memory2, instance3);
                }
                case 1451: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1451(n5, memory2, instance3);
                }
                case 1453: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1453(n5, memory2, instance3);
                }
                case 1458: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1458(n5, memory2, instance3);
                }
                case 1460: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1460(n5, memory2, instance3);
                }
                case 1462: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1462(n5, memory2, instance3);
                }
                case 1465: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1465(n5, memory2, instance3);
                }
                case 1467: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1467(n5, memory2, instance3);
                }
                case 1469: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1469(n5, memory2, instance3);
                }
                case 1471: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1471(n5, memory2, instance3);
                }
                case 1473: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1473(n5, memory2, instance3);
                }
                case 1475: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1475(n5, memory2, instance3);
                }
                case 1477: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1477(n5, memory2, instance3);
                }
                case 1479: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1479(n5, memory2, instance3);
                }
                case 1481: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1481(n5, memory2, instance3);
                }
                case 1484: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1484(n5, memory2, instance3);
                }
                case 1487: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1487(n5, memory2, instance3);
                }
                case 1493: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1493(n5, memory2, instance3);
                }
                case 1495: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1495(n5, memory2, instance3);
                }
                case 1497: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1497(n5, memory2, instance3);
                }
                case 1499: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1499(n5, memory2, instance3);
                }
                case 1501: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1501(n5, memory2, instance3);
                }
                case 1503: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1503(n5, memory2, instance3);
                }
                case 1505: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1505(n5, memory2, instance3);
                }
                case 1507: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1507(n5, memory2, instance3);
                }
                case 1509: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1509(n5, memory2, instance3);
                }
                case 1511: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1511(n5, memory2, instance3);
                }
                case 1513: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1513(n5, memory2, instance3);
                }
                case 1515: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1515(n5, memory2, instance3);
                }
                case 1517: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1517(n5, memory2, instance3);
                }
                case 1521: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1521(n5, memory2, instance3);
                }
                case 1524: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1524(n5, memory2, instance3);
                }
                case 1526: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1526(n5, memory2, instance3);
                }
                case 1529: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1529(n5, memory2, instance3);
                }
                case 1532: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1532(n5, memory2, instance3);
                }
                case 1535: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1535(n5, memory2, instance3);
                }
                case 1538: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1538(n5, memory2, instance3);
                }
                case 1541: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1541(n5, memory2, instance3);
                }
                case 1545: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1545(n5, memory2, instance3);
                }
                case 1548: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1548(n5, memory2, instance3);
                }
                case 1551: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1551(n5, memory2, instance3);
                }
                case 1553: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1553(n5, memory2, instance3);
                }
                case 1555: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1555(n5, memory2, instance3);
                }
                case 1557: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1557(n5, memory2, instance3);
                }
                case 1559: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1559(n5, memory2, instance3);
                }
                case 1561: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1561(n5, memory2, instance3);
                }
                case 1563: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1563(n5, memory2, instance3);
                }
                case 1565: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1565(n5, memory2, instance3);
                }
                case 1566: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1566(n5, memory2, instance3);
                }
                case 1568: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1568(n5, memory2, instance3);
                }
                case 1569: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1569(n5, memory2, instance3);
                }
                case 1571: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1571(n5, memory2, instance3);
                }
                case 1572: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1572(n5, memory2, instance3);
                }
                case 1579: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1579(n5, memory2, instance3);
                }
                case 1588: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1588(n5, memory2, instance3);
                }
                case 1590: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1590(n5, memory2, instance3);
                }
                case 1593: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1593(n5, memory2, instance3);
                }
                case 1594: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1594(n5, memory2, instance3);
                }
                case 1595: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1595(n5, memory2, instance3);
                }
                case 1599: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1599(n5, memory2, instance3);
                }
                case 1600: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1600(n5, memory2, instance3);
                }
                case 1601: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1601(n5, memory2, instance3);
                }
                case 1604: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1604(n5, memory2, instance3);
                }
                case 1605: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1605(n5, memory2, instance3);
                }
                case 1606: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1606(n5, memory2, instance3);
                }
                case 1611: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1611(n5, memory2, instance3);
                }
                case 1614: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1614(n5, memory2, instance3);
                }
                case 1615: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1615(n5, memory2, instance3);
                }
                case 1624: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1624(n5, memory2, instance3);
                }
                case 1625: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1625(n5, memory2, instance3);
                }
                case 1626: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1626(n5, memory2, instance3);
                }
                case 1627: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1627(n5, memory2, instance3);
                }
                case 1629: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1629(n5, memory2, instance3);
                }
                case 1630: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1630(n5, memory2, instance3);
                }
                case 1633: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1633(n5, memory2, instance3);
                }
                case 1634: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1634(n5, memory2, instance3);
                }
                case 1636: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1636(n5, memory2, instance3);
                }
                case 1637: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1637(n5, memory2, instance3);
                }
                case 1639: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1639(n5, memory2, instance3);
                }
                case 1640: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1640(n5, memory2, instance3);
                }
                case 1641: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1641(n5, memory2, instance3);
                }
                case 1656: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1656(n5, memory2, instance3);
                }
                case 1672: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1672(n5, memory2, instance3);
                }
                case 1673: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1673(n5, memory2, instance3);
                }
                case 1674: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1674(n5, memory2, instance3);
                }
                case 1675: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1675(n5, memory2, instance3);
                }
                case 1676: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1676(n5, memory2, instance3);
                }
                case 1678: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1678(n5, memory2, instance3);
                }
                case 1679: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1679(n5, memory2, instance3);
                }
                case 1681: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1681(n5, memory2, instance3);
                }
                case 1683: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1683(n5, memory2, instance3);
                }
                case 1684: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1684(n5, memory2, instance3);
                }
                case 1690: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1690(n5, memory2, instance3);
                }
                case 1697: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1697(n5, memory2, instance3);
                }
                case 1701: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1701(n5, memory2, instance3);
                }
                case 1703: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1703(n5, memory2, instance3);
                }
                case 1713: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1713(n5, memory2, instance3);
                }
                case 1715: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1715(n5, memory2, instance3);
                }
                case 1716: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1716(n5, memory2, instance3);
                }
                case 1717: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1717(n5, memory2, instance3);
                }
                case 1720: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1720(n5, memory2, instance3);
                }
                case 1721: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1721(n5, memory2, instance3);
                }
                case 1723: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1723(n5, memory2, instance3);
                }
                case 1725: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1725(n5, memory2, instance3);
                }
                case 1740: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1740(n5, memory2, instance3);
                }
                case 1741: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1741(n5, memory2, instance3);
                }
                case 1744: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1744(n5, memory2, instance3);
                }
                case 1746: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1746(n5, memory2, instance3);
                }
                case 1748: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1748(n5, memory2, instance3);
                }
                case 1750: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1750(n5, memory2, instance3);
                }
                case 1755: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1755(n5, memory2, instance3);
                }
                case 1776: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1776(n5, memory2, instance3);
                }
                case 1782: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1782(n5, memory2, instance3);
                }
                case 1783: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1783(n5, memory2, instance3);
                }
                case 1796: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1796(n5, memory2, instance3);
                }
                case 1808: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1808(n5, memory2, instance3);
                }
                case 1811: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1811(n5, memory2, instance3);
                }
                case 1818: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1818(n5, memory2, instance3);
                }
                case 1819: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1819(n5, memory2, instance3);
                }
                case 1822: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1822(n5, memory2, instance3);
                }
                case 1823: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1823(n5, memory2, instance3);
                }
                case 1831: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1831(n5, memory2, instance3);
                }
                case 1832: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1832(n5, memory2, instance3);
                }
                case 1833: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1833(n5, memory2, instance3);
                }
                case 1834: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1834(n5, memory2, instance3);
                }
                case 1835: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1835(n5, memory2, instance3);
                }
                case 1844: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1844(n5, memory2, instance3);
                }
                case 1850: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1850(n5, memory2, instance3);
                }
                case 1853: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1853(n5, memory2, instance3);
                }
                case 1854: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1854(n5, memory2, instance3);
                }
                case 1859: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1859(n5, memory2, instance3);
                }
                case 1864: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1864(n5, memory2, instance3);
                }
                case 1877: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1877(n5, memory2, instance3);
                }
                case 1878: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1878(n5, memory2, instance3);
                }
                case 1880: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1880(n5, memory2, instance3);
                }
                case 1896: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1896(n5, memory2, instance3);
                }
                case 1897: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1897(n5, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n}, 1, n4, instance2)[0];
    }

    public static void call_indirect_2(int n, int n2, int n3, int n4, int n5, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n5);
        int n6 = tableInstance.requiredRef(n4);
        Instance instance2 = tableInstance.instance(n4);
        if (instance2 == null || instance2 == instance) {
            int n7 = n;
            int n8 = n2;
            int n9 = n3;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n6) {
                case 37: {
                    Wat2WasmModuleMachineFuncGroup_0.func_37(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 39: {
                    Wat2WasmModuleMachineFuncGroup_0.func_39(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 51: {
                    Wat2WasmModuleMachineFuncGroup_0.func_51(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 53: {
                    Wat2WasmModuleMachineFuncGroup_0.func_53(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 55: {
                    Wat2WasmModuleMachineFuncGroup_0.func_55(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 64: {
                    Wat2WasmModuleMachineFuncGroup_0.func_64(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 115: {
                    Wat2WasmModuleMachineFuncGroup_0.func_115(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 117: {
                    Wat2WasmModuleMachineFuncGroup_0.func_117(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 152: {
                    Wat2WasmModuleMachineFuncGroup_0.func_152(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 169: {
                    Wat2WasmModuleMachineFuncGroup_0.func_169(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 170: {
                    Wat2WasmModuleMachineFuncGroup_0.func_170(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 173: {
                    Wat2WasmModuleMachineFuncGroup_0.func_173(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 176: {
                    Wat2WasmModuleMachineFuncGroup_0.func_176(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 205: {
                    Wat2WasmModuleMachineFuncGroup_0.func_205(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 211: {
                    Wat2WasmModuleMachineFuncGroup_0.func_211(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 212: {
                    Wat2WasmModuleMachineFuncGroup_0.func_212(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 223: {
                    Wat2WasmModuleMachineFuncGroup_0.func_223(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 246: {
                    Wat2WasmModuleMachineFuncGroup_0.func_246(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 254: {
                    Wat2WasmModuleMachineFuncGroup_0.func_254(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 344: {
                    Wat2WasmModuleMachineFuncGroup_0.func_344(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 545: {
                    Wat2WasmModuleMachineFuncGroup_0.func_545(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 561: {
                    Wat2WasmModuleMachineFuncGroup_0.func_561(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 567: {
                    Wat2WasmModuleMachineFuncGroup_0.func_567(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 573: {
                    Wat2WasmModuleMachineFuncGroup_0.func_573(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 802: {
                    Wat2WasmModuleMachineFuncGroup_0.func_802(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 853: {
                    Wat2WasmModuleMachineFuncGroup_0.func_853(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 862: {
                    Wat2WasmModuleMachineFuncGroup_0.func_862(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 900: {
                    Wat2WasmModuleMachineFuncGroup_0.func_900(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 925: {
                    Wat2WasmModuleMachineFuncGroup_0.func_925(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1023: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1023(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1027: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1027(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1099: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1099(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1101: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1101(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1215: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1215(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1234: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1234(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1304: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1304(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1320: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1320(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1616: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1616(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1620: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1620(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1642: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1642(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1646: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1646(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1647: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1647(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1689: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1689(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1694: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1694(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1695: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1695(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1704: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1704(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1708: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1708(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1709: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1709(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1719: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1719(n7, n8, n9, memory2, instance3);
                    return;
                }
                case 1872: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1872(n7, n8, n9, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3}, 2, n6, instance2);
    }

    public static void call_indirect_3(int n, int n2, int n3, int n4, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n4);
        int n5 = tableInstance.requiredRef(n3);
        Instance instance2 = tableInstance.instance(n3);
        if (instance2 == null || instance2 == instance) {
            int n6 = n;
            int n7 = n2;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n5) {
                case 25: {
                    Wat2WasmModuleMachineFuncGroup_0.func_25(n6, n7, memory2, instance3);
                    return;
                }
                case 30: {
                    Wat2WasmModuleMachineFuncGroup_0.func_30(n6, n7, memory2, instance3);
                    return;
                }
                case 34: {
                    Wat2WasmModuleMachineFuncGroup_0.func_34(n6, n7, memory2, instance3);
                    return;
                }
                case 36: {
                    Wat2WasmModuleMachineFuncGroup_0.func_36(n6, n7, memory2, instance3);
                    return;
                }
                case 38: {
                    Wat2WasmModuleMachineFuncGroup_0.func_38(n6, n7, memory2, instance3);
                    return;
                }
                case 42: {
                    Wat2WasmModuleMachineFuncGroup_0.func_42(n6, n7, memory2, instance3);
                    return;
                }
                case 43: {
                    Wat2WasmModuleMachineFuncGroup_0.func_43(n6, n7, memory2, instance3);
                    return;
                }
                case 47: {
                    Wat2WasmModuleMachineFuncGroup_0.func_47(n6, n7, memory2, instance3);
                    return;
                }
                case 49: {
                    Wat2WasmModuleMachineFuncGroup_0.func_49(n6, n7, memory2, instance3);
                    return;
                }
                case 50: {
                    Wat2WasmModuleMachineFuncGroup_0.func_50(n6, n7, memory2, instance3);
                    return;
                }
                case 52: {
                    Wat2WasmModuleMachineFuncGroup_0.func_52(n6, n7, memory2, instance3);
                    return;
                }
                case 56: {
                    Wat2WasmModuleMachineFuncGroup_0.func_56(n6, n7, memory2, instance3);
                    return;
                }
                case 57: {
                    Wat2WasmModuleMachineFuncGroup_0.func_57(n6, n7, memory2, instance3);
                    return;
                }
                case 58: {
                    Wat2WasmModuleMachineFuncGroup_0.func_58(n6, n7, memory2, instance3);
                    return;
                }
                case 59: {
                    Wat2WasmModuleMachineFuncGroup_0.func_59(n6, n7, memory2, instance3);
                    return;
                }
                case 60: {
                    Wat2WasmModuleMachineFuncGroup_0.func_60(n6, n7, memory2, instance3);
                    return;
                }
                case 61: {
                    Wat2WasmModuleMachineFuncGroup_0.func_61(n6, n7, memory2, instance3);
                    return;
                }
                case 62: {
                    Wat2WasmModuleMachineFuncGroup_0.func_62(n6, n7, memory2, instance3);
                    return;
                }
                case 65: {
                    Wat2WasmModuleMachineFuncGroup_0.func_65(n6, n7, memory2, instance3);
                    return;
                }
                case 66: {
                    Wat2WasmModuleMachineFuncGroup_0.func_66(n6, n7, memory2, instance3);
                    return;
                }
                case 67: {
                    Wat2WasmModuleMachineFuncGroup_0.func_67(n6, n7, memory2, instance3);
                    return;
                }
                case 68: {
                    Wat2WasmModuleMachineFuncGroup_0.func_68(n6, n7, memory2, instance3);
                    return;
                }
                case 69: {
                    Wat2WasmModuleMachineFuncGroup_0.func_69(n6, n7, memory2, instance3);
                    return;
                }
                case 70: {
                    Wat2WasmModuleMachineFuncGroup_0.func_70(n6, n7, memory2, instance3);
                    return;
                }
                case 72: {
                    Wat2WasmModuleMachineFuncGroup_0.func_72(n6, n7, memory2, instance3);
                    return;
                }
                case 84: {
                    Wat2WasmModuleMachineFuncGroup_0.func_84(n6, n7, memory2, instance3);
                    return;
                }
                case 89: {
                    Wat2WasmModuleMachineFuncGroup_0.func_89(n6, n7, memory2, instance3);
                    return;
                }
                case 110: {
                    Wat2WasmModuleMachineFuncGroup_0.func_110(n6, n7, memory2, instance3);
                    return;
                }
                case 111: {
                    Wat2WasmModuleMachineFuncGroup_0.func_111(n6, n7, memory2, instance3);
                    return;
                }
                case 112: {
                    Wat2WasmModuleMachineFuncGroup_0.func_112(n6, n7, memory2, instance3);
                    return;
                }
                case 113: {
                    Wat2WasmModuleMachineFuncGroup_0.func_113(n6, n7, memory2, instance3);
                    return;
                }
                case 129: {
                    Wat2WasmModuleMachineFuncGroup_0.func_129(n6, n7, memory2, instance3);
                    return;
                }
                case 136: {
                    Wat2WasmModuleMachineFuncGroup_0.func_136(n6, n7, memory2, instance3);
                    return;
                }
                case 139: {
                    Wat2WasmModuleMachineFuncGroup_0.func_139(n6, n7, memory2, instance3);
                    return;
                }
                case 140: {
                    Wat2WasmModuleMachineFuncGroup_0.func_140(n6, n7, memory2, instance3);
                    return;
                }
                case 141: {
                    Wat2WasmModuleMachineFuncGroup_0.func_141(n6, n7, memory2, instance3);
                    return;
                }
                case 142: {
                    Wat2WasmModuleMachineFuncGroup_0.func_142(n6, n7, memory2, instance3);
                    return;
                }
                case 143: {
                    Wat2WasmModuleMachineFuncGroup_0.func_143(n6, n7, memory2, instance3);
                    return;
                }
                case 144: {
                    Wat2WasmModuleMachineFuncGroup_0.func_144(n6, n7, memory2, instance3);
                    return;
                }
                case 145: {
                    Wat2WasmModuleMachineFuncGroup_0.func_145(n6, n7, memory2, instance3);
                    return;
                }
                case 147: {
                    Wat2WasmModuleMachineFuncGroup_0.func_147(n6, n7, memory2, instance3);
                    return;
                }
                case 148: {
                    Wat2WasmModuleMachineFuncGroup_0.func_148(n6, n7, memory2, instance3);
                    return;
                }
                case 149: {
                    Wat2WasmModuleMachineFuncGroup_0.func_149(n6, n7, memory2, instance3);
                    return;
                }
                case 150: {
                    Wat2WasmModuleMachineFuncGroup_0.func_150(n6, n7, memory2, instance3);
                    return;
                }
                case 151: {
                    Wat2WasmModuleMachineFuncGroup_0.func_151(n6, n7, memory2, instance3);
                    return;
                }
                case 153: {
                    Wat2WasmModuleMachineFuncGroup_0.func_153(n6, n7, memory2, instance3);
                    return;
                }
                case 161: {
                    Wat2WasmModuleMachineFuncGroup_0.func_161(n6, n7, memory2, instance3);
                    return;
                }
                case 166: {
                    Wat2WasmModuleMachineFuncGroup_0.func_166(n6, n7, memory2, instance3);
                    return;
                }
                case 191: {
                    Wat2WasmModuleMachineFuncGroup_0.func_191(n6, n7, memory2, instance3);
                    return;
                }
                case 192: {
                    Wat2WasmModuleMachineFuncGroup_0.func_192(n6, n7, memory2, instance3);
                    return;
                }
                case 196: {
                    Wat2WasmModuleMachineFuncGroup_0.func_196(n6, n7, memory2, instance3);
                    return;
                }
                case 199: {
                    Wat2WasmModuleMachineFuncGroup_0.func_199(n6, n7, memory2, instance3);
                    return;
                }
                case 203: {
                    Wat2WasmModuleMachineFuncGroup_0.func_203(n6, n7, memory2, instance3);
                    return;
                }
                case 204: {
                    Wat2WasmModuleMachineFuncGroup_0.func_204(n6, n7, memory2, instance3);
                    return;
                }
                case 209: {
                    Wat2WasmModuleMachineFuncGroup_0.func_209(n6, n7, memory2, instance3);
                    return;
                }
                case 214: {
                    Wat2WasmModuleMachineFuncGroup_0.func_214(n6, n7, memory2, instance3);
                    return;
                }
                case 220: {
                    Wat2WasmModuleMachineFuncGroup_0.func_220(n6, n7, memory2, instance3);
                    return;
                }
                case 226: {
                    Wat2WasmModuleMachineFuncGroup_0.func_226(n6, n7, memory2, instance3);
                    return;
                }
                case 248: {
                    Wat2WasmModuleMachineFuncGroup_0.func_248(n6, n7, memory2, instance3);
                    return;
                }
                case 252: {
                    Wat2WasmModuleMachineFuncGroup_0.func_252(n6, n7, memory2, instance3);
                    return;
                }
                case 336: {
                    Wat2WasmModuleMachineFuncGroup_0.func_336(n6, n7, memory2, instance3);
                    return;
                }
                case 343: {
                    Wat2WasmModuleMachineFuncGroup_0.func_343(n6, n7, memory2, instance3);
                    return;
                }
                case 351: {
                    Wat2WasmModuleMachineFuncGroup_0.func_351(n6, n7, memory2, instance3);
                    return;
                }
                case 362: {
                    Wat2WasmModuleMachineFuncGroup_0.func_362(n6, n7, memory2, instance3);
                    return;
                }
                case 367: {
                    Wat2WasmModuleMachineFuncGroup_0.func_367(n6, n7, memory2, instance3);
                    return;
                }
                case 417: {
                    Wat2WasmModuleMachineFuncGroup_0.func_417(n6, n7, memory2, instance3);
                    return;
                }
                case 449: {
                    Wat2WasmModuleMachineFuncGroup_0.func_449(n6, n7, memory2, instance3);
                    return;
                }
                case 534: {
                    Wat2WasmModuleMachineFuncGroup_0.func_534(n6, n7, memory2, instance3);
                    return;
                }
                case 535: {
                    Wat2WasmModuleMachineFuncGroup_0.func_535(n6, n7, memory2, instance3);
                    return;
                }
                case 536: {
                    Wat2WasmModuleMachineFuncGroup_0.func_536(n6, n7, memory2, instance3);
                    return;
                }
                case 537: {
                    Wat2WasmModuleMachineFuncGroup_0.func_537(n6, n7, memory2, instance3);
                    return;
                }
                case 544: {
                    Wat2WasmModuleMachineFuncGroup_0.func_544(n6, n7, memory2, instance3);
                    return;
                }
                case 557: {
                    Wat2WasmModuleMachineFuncGroup_0.func_557(n6, n7, memory2, instance3);
                    return;
                }
                case 558: {
                    Wat2WasmModuleMachineFuncGroup_0.func_558(n6, n7, memory2, instance3);
                    return;
                }
                case 559: {
                    Wat2WasmModuleMachineFuncGroup_0.func_559(n6, n7, memory2, instance3);
                    return;
                }
                case 560: {
                    Wat2WasmModuleMachineFuncGroup_0.func_560(n6, n7, memory2, instance3);
                    return;
                }
                case 562: {
                    Wat2WasmModuleMachineFuncGroup_0.func_562(n6, n7, memory2, instance3);
                    return;
                }
                case 563: {
                    Wat2WasmModuleMachineFuncGroup_0.func_563(n6, n7, memory2, instance3);
                    return;
                }
                case 565: {
                    Wat2WasmModuleMachineFuncGroup_0.func_565(n6, n7, memory2, instance3);
                    return;
                }
                case 574: {
                    Wat2WasmModuleMachineFuncGroup_0.func_574(n6, n7, memory2, instance3);
                    return;
                }
                case 576: {
                    Wat2WasmModuleMachineFuncGroup_0.func_576(n6, n7, memory2, instance3);
                    return;
                }
                case 809: {
                    Wat2WasmModuleMachineFuncGroup_0.func_809(n6, n7, memory2, instance3);
                    return;
                }
                case 816: {
                    Wat2WasmModuleMachineFuncGroup_0.func_816(n6, n7, memory2, instance3);
                    return;
                }
                case 820: {
                    Wat2WasmModuleMachineFuncGroup_0.func_820(n6, n7, memory2, instance3);
                    return;
                }
                case 822: {
                    Wat2WasmModuleMachineFuncGroup_0.func_822(n6, n7, memory2, instance3);
                    return;
                }
                case 830: {
                    Wat2WasmModuleMachineFuncGroup_0.func_830(n6, n7, memory2, instance3);
                    return;
                }
                case 833: {
                    Wat2WasmModuleMachineFuncGroup_0.func_833(n6, n7, memory2, instance3);
                    return;
                }
                case 834: {
                    Wat2WasmModuleMachineFuncGroup_0.func_834(n6, n7, memory2, instance3);
                    return;
                }
                case 835: {
                    Wat2WasmModuleMachineFuncGroup_0.func_835(n6, n7, memory2, instance3);
                    return;
                }
                case 836: {
                    Wat2WasmModuleMachineFuncGroup_0.func_836(n6, n7, memory2, instance3);
                    return;
                }
                case 837: {
                    Wat2WasmModuleMachineFuncGroup_0.func_837(n6, n7, memory2, instance3);
                    return;
                }
                case 843: {
                    Wat2WasmModuleMachineFuncGroup_0.func_843(n6, n7, memory2, instance3);
                    return;
                }
                case 844: {
                    Wat2WasmModuleMachineFuncGroup_0.func_844(n6, n7, memory2, instance3);
                    return;
                }
                case 855: {
                    Wat2WasmModuleMachineFuncGroup_0.func_855(n6, n7, memory2, instance3);
                    return;
                }
                case 929: {
                    Wat2WasmModuleMachineFuncGroup_0.func_929(n6, n7, memory2, instance3);
                    return;
                }
                case 984: {
                    Wat2WasmModuleMachineFuncGroup_0.func_984(n6, n7, memory2, instance3);
                    return;
                }
                case 1003: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1003(n6, n7, memory2, instance3);
                    return;
                }
                case 1058: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1058(n6, n7, memory2, instance3);
                    return;
                }
                case 1094: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1094(n6, n7, memory2, instance3);
                    return;
                }
                case 1095: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1095(n6, n7, memory2, instance3);
                    return;
                }
                case 1096: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1096(n6, n7, memory2, instance3);
                    return;
                }
                case 1105: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1105(n6, n7, memory2, instance3);
                    return;
                }
                case 1108: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1108(n6, n7, memory2, instance3);
                    return;
                }
                case 1153: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1153(n6, n7, memory2, instance3);
                    return;
                }
                case 1162: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1162(n6, n7, memory2, instance3);
                    return;
                }
                case 1185: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1185(n6, n7, memory2, instance3);
                    return;
                }
                case 1188: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1188(n6, n7, memory2, instance3);
                    return;
                }
                case 1189: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1189(n6, n7, memory2, instance3);
                    return;
                }
                case 1210: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1210(n6, n7, memory2, instance3);
                    return;
                }
                case 1218: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1218(n6, n7, memory2, instance3);
                    return;
                }
                case 1231: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1231(n6, n7, memory2, instance3);
                    return;
                }
                case 1252: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1252(n6, n7, memory2, instance3);
                    return;
                }
                case 1283: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1283(n6, n7, memory2, instance3);
                    return;
                }
                case 1303: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1303(n6, n7, memory2, instance3);
                    return;
                }
                case 1306: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1306(n6, n7, memory2, instance3);
                    return;
                }
                case 1409: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1409(n6, n7, memory2, instance3);
                    return;
                }
                case 1413: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1413(n6, n7, memory2, instance3);
                    return;
                }
                case 1580: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1580(n6, n7, memory2, instance3);
                    return;
                }
                case 1584: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1584(n6, n7, memory2, instance3);
                    return;
                }
                case 1589: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1589(n6, n7, memory2, instance3);
                    return;
                }
                case 1608: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1608(n6, n7, memory2, instance3);
                    return;
                }
                case 1609: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1609(n6, n7, memory2, instance3);
                    return;
                }
                case 1610: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1610(n6, n7, memory2, instance3);
                    return;
                }
                case 1617: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1617(n6, n7, memory2, instance3);
                    return;
                }
                case 1621: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1621(n6, n7, memory2, instance3);
                    return;
                }
                case 1622: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1622(n6, n7, memory2, instance3);
                    return;
                }
                case 1623: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1623(n6, n7, memory2, instance3);
                    return;
                }
                case 1628: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1628(n6, n7, memory2, instance3);
                    return;
                }
                case 1660: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1660(n6, n7, memory2, instance3);
                    return;
                }
                case 1664: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1664(n6, n7, memory2, instance3);
                    return;
                }
                case 1665: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1665(n6, n7, memory2, instance3);
                    return;
                }
                case 1668: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1668(n6, n7, memory2, instance3);
                    return;
                }
                case 1669: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1669(n6, n7, memory2, instance3);
                    return;
                }
                case 1710: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1710(n6, n7, memory2, instance3);
                    return;
                }
                case 1711: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1711(n6, n7, memory2, instance3);
                    return;
                }
                case 1780: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1780(n6, n7, memory2, instance3);
                    return;
                }
                case 1787: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1787(n6, n7, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2}, 3, n5, instance2);
    }

    public static int call_indirect_4(int n, int n2, int n3, int n4, int n5, int n6, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n6);
        int n7 = tableInstance.requiredRef(n5);
        Instance instance2 = tableInstance.instance(n5);
        if (instance2 == null || instance2 == instance) {
            int n8 = n;
            int n9 = n2;
            int n10 = n3;
            int n11 = n4;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n7) {
                case 9: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_9(n8, n9, n10, n11, memory2, instance3);
                }
                case 11: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_11(n8, n9, n10, n11, memory2, instance3);
                }
                case 48: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_48(n8, n9, n10, n11, memory2, instance3);
                }
                case 77: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_77(n8, n9, n10, n11, memory2, instance3);
                }
                case 168: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_168(n8, n9, n10, n11, memory2, instance3);
                }
                case 171: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_171(n8, n9, n10, n11, memory2, instance3);
                }
                case 182: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_182(n8, n9, n10, n11, memory2, instance3);
                }
                case 197: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_197(n8, n9, n10, n11, memory2, instance3);
                }
                case 231: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_231(n8, n9, n10, n11, memory2, instance3);
                }
                case 232: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_232(n8, n9, n10, n11, memory2, instance3);
                }
                case 237: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_237(n8, n9, n10, n11, memory2, instance3);
                }
                case 238: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_238(n8, n9, n10, n11, memory2, instance3);
                }
                case 256: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_256(n8, n9, n10, n11, memory2, instance3);
                }
                case 258: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_258(n8, n9, n10, n11, memory2, instance3);
                }
                case 277: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_277(n8, n9, n10, n11, memory2, instance3);
                }
                case 288: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_288(n8, n9, n10, n11, memory2, instance3);
                }
                case 337: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_337(n8, n9, n10, n11, memory2, instance3);
                }
                case 339: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_339(n8, n9, n10, n11, memory2, instance3);
                }
                case 345: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_345(n8, n9, n10, n11, memory2, instance3);
                }
                case 346: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_346(n8, n9, n10, n11, memory2, instance3);
                }
                case 347: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_347(n8, n9, n10, n11, memory2, instance3);
                }
                case 348: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_348(n8, n9, n10, n11, memory2, instance3);
                }
                case 360: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_360(n8, n9, n10, n11, memory2, instance3);
                }
                case 363: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_363(n8, n9, n10, n11, memory2, instance3);
                }
                case 372: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_372(n8, n9, n10, n11, memory2, instance3);
                }
                case 388: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_388(n8, n9, n10, n11, memory2, instance3);
                }
                case 390: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_390(n8, n9, n10, n11, memory2, instance3);
                }
                case 410: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_410(n8, n9, n10, n11, memory2, instance3);
                }
                case 413: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_413(n8, n9, n10, n11, memory2, instance3);
                }
                case 422: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_422(n8, n9, n10, n11, memory2, instance3);
                }
                case 424: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_424(n8, n9, n10, n11, memory2, instance3);
                }
                case 428: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_428(n8, n9, n10, n11, memory2, instance3);
                }
                case 430: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_430(n8, n9, n10, n11, memory2, instance3);
                }
                case 434: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_434(n8, n9, n10, n11, memory2, instance3);
                }
                case 540: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_540(n8, n9, n10, n11, memory2, instance3);
                }
                case 541: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_541(n8, n9, n10, n11, memory2, instance3);
                }
                case 542: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_542(n8, n9, n10, n11, memory2, instance3);
                }
                case 543: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_543(n8, n9, n10, n11, memory2, instance3);
                }
                case 548: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_548(n8, n9, n10, n11, memory2, instance3);
                }
                case 549: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_549(n8, n9, n10, n11, memory2, instance3);
                }
                case 550: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_550(n8, n9, n10, n11, memory2, instance3);
                }
                case 551: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_551(n8, n9, n10, n11, memory2, instance3);
                }
                case 552: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_552(n8, n9, n10, n11, memory2, instance3);
                }
                case 554: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_554(n8, n9, n10, n11, memory2, instance3);
                }
                case 556: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_556(n8, n9, n10, n11, memory2, instance3);
                }
                case 578: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_578(n8, n9, n10, n11, memory2, instance3);
                }
                case 579: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_579(n8, n9, n10, n11, memory2, instance3);
                }
                case 581: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_581(n8, n9, n10, n11, memory2, instance3);
                }
                case 589: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_589(n8, n9, n10, n11, memory2, instance3);
                }
                case 590: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_590(n8, n9, n10, n11, memory2, instance3);
                }
                case 591: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_591(n8, n9, n10, n11, memory2, instance3);
                }
                case 594: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_594(n8, n9, n10, n11, memory2, instance3);
                }
                case 598: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_598(n8, n9, n10, n11, memory2, instance3);
                }
                case 611: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_611(n8, n9, n10, n11, memory2, instance3);
                }
                case 614: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_614(n8, n9, n10, n11, memory2, instance3);
                }
                case 616: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_616(n8, n9, n10, n11, memory2, instance3);
                }
                case 618: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_618(n8, n9, n10, n11, memory2, instance3);
                }
                case 619: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_619(n8, n9, n10, n11, memory2, instance3);
                }
                case 620: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_620(n8, n9, n10, n11, memory2, instance3);
                }
                case 621: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_621(n8, n9, n10, n11, memory2, instance3);
                }
                case 625: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_625(n8, n9, n10, n11, memory2, instance3);
                }
                case 632: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_632(n8, n9, n10, n11, memory2, instance3);
                }
                case 637: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_637(n8, n9, n10, n11, memory2, instance3);
                }
                case 641: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_641(n8, n9, n10, n11, memory2, instance3);
                }
                case 746: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_746(n8, n9, n10, n11, memory2, instance3);
                }
                case 791: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_791(n8, n9, n10, n11, memory2, instance3);
                }
                case 801: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_801(n8, n9, n10, n11, memory2, instance3);
                }
                case 847: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_847(n8, n9, n10, n11, memory2, instance3);
                }
                case 848: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_848(n8, n9, n10, n11, memory2, instance3);
                }
                case 854: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_854(n8, n9, n10, n11, memory2, instance3);
                }
                case 874: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_874(n8, n9, n10, n11, memory2, instance3);
                }
                case 878: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_878(n8, n9, n10, n11, memory2, instance3);
                }
                case 882: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_882(n8, n9, n10, n11, memory2, instance3);
                }
                case 884: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_884(n8, n9, n10, n11, memory2, instance3);
                }
                case 899: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_899(n8, n9, n10, n11, memory2, instance3);
                }
                case 908: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_908(n8, n9, n10, n11, memory2, instance3);
                }
                case 928: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_928(n8, n9, n10, n11, memory2, instance3);
                }
                case 998: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_998(n8, n9, n10, n11, memory2, instance3);
                }
                case 1011: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1011(n8, n9, n10, n11, memory2, instance3);
                }
                case 1021: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1021(n8, n9, n10, n11, memory2, instance3);
                }
                case 1024: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1024(n8, n9, n10, n11, memory2, instance3);
                }
                case 1028: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1028(n8, n9, n10, n11, memory2, instance3);
                }
                case 1031: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1031(n8, n9, n10, n11, memory2, instance3);
                }
                case 1033: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1033(n8, n9, n10, n11, memory2, instance3);
                }
                case 1035: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1035(n8, n9, n10, n11, memory2, instance3);
                }
                case 1047: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1047(n8, n9, n10, n11, memory2, instance3);
                }
                case 1057: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1057(n8, n9, n10, n11, memory2, instance3);
                }
                case 1065: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1065(n8, n9, n10, n11, memory2, instance3);
                }
                case 1073: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1073(n8, n9, n10, n11, memory2, instance3);
                }
                case 1216: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1216(n8, n9, n10, n11, memory2, instance3);
                }
                case 1238: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1238(n8, n9, n10, n11, memory2, instance3);
                }
                case 1272: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1272(n8, n9, n10, n11, memory2, instance3);
                }
                case 1273: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1273(n8, n9, n10, n11, memory2, instance3);
                }
                case 1281: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1281(n8, n9, n10, n11, memory2, instance3);
                }
                case 1284: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1284(n8, n9, n10, n11, memory2, instance3);
                }
                case 1288: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1288(n8, n9, n10, n11, memory2, instance3);
                }
                case 1289: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1289(n8, n9, n10, n11, memory2, instance3);
                }
                case 1290: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1290(n8, n9, n10, n11, memory2, instance3);
                }
                case 1291: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1291(n8, n9, n10, n11, memory2, instance3);
                }
                case 1292: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1292(n8, n9, n10, n11, memory2, instance3);
                }
                case 1293: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1293(n8, n9, n10, n11, memory2, instance3);
                }
                case 1295: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1295(n8, n9, n10, n11, memory2, instance3);
                }
                case 1296: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1296(n8, n9, n10, n11, memory2, instance3);
                }
                case 1301: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1301(n8, n9, n10, n11, memory2, instance3);
                }
                case 1307: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1307(n8, n9, n10, n11, memory2, instance3);
                }
                case 1648: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1648(n8, n9, n10, n11, memory2, instance3);
                }
                case 1757: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1757(n8, n9, n10, n11, memory2, instance3);
                }
                case 1801: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1801(n8, n9, n10, n11, memory2, instance3);
                }
                case 1803: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1803(n8, n9, n10, n11, memory2, instance3);
                }
                case 1804: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1804(n8, n9, n10, n11, memory2, instance3);
                }
                case 1810: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1810(n8, n9, n10, n11, memory2, instance3);
                }
                case 1815: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1815(n8, n9, n10, n11, memory2, instance3);
                }
                case 1851: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1851(n8, n9, n10, n11, memory2, instance3);
                }
                case 1856: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1856(n8, n9, n10, n11, memory2, instance3);
                }
                case 1865: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1865(n8, n9, n10, n11, memory2, instance3);
                }
                case 1875: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1875(n8, n9, n10, n11, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4}, 4, n7, instance2)[0];
    }

    public static int call_indirect_5(int n, int n2, int n3, int n4, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n4);
        int n5 = tableInstance.requiredRef(n3);
        Instance instance2 = tableInstance.instance(n3);
        if (instance2 == null || instance2 == instance) {
            int n6 = n;
            int n7 = n2;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n5) {
                case 0: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_0(n6, n7, memory2, instance3);
                }
                case 1: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1(n6, n7, memory2, instance3);
                }
                case 2: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_2(n6, n7, memory2, instance3);
                }
                case 3: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_3(n6, n7, memory2, instance3);
                }
                case 5: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_5(n6, n7, memory2, instance3);
                }
                case 6: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_6(n6, n7, memory2, instance3);
                }
                case 7: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_7(n6, n7, memory2, instance3);
                }
                case 21: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_21(n6, n7, memory2, instance3);
                }
                case 33: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_33(n6, n7, memory2, instance3);
                }
                case 44: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_44(n6, n7, memory2, instance3);
                }
                case 71: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_71(n6, n7, memory2, instance3);
                }
                case 82: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_82(n6, n7, memory2, instance3);
                }
                case 116: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_116(n6, n7, memory2, instance3);
                }
                case 119: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_119(n6, n7, memory2, instance3);
                }
                case 120: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_120(n6, n7, memory2, instance3);
                }
                case 121: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_121(n6, n7, memory2, instance3);
                }
                case 122: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_122(n6, n7, memory2, instance3);
                }
                case 123: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_123(n6, n7, memory2, instance3);
                }
                case 124: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_124(n6, n7, memory2, instance3);
                }
                case 125: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_125(n6, n7, memory2, instance3);
                }
                case 126: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_126(n6, n7, memory2, instance3);
                }
                case 127: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_127(n6, n7, memory2, instance3);
                }
                case 128: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_128(n6, n7, memory2, instance3);
                }
                case 131: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_131(n6, n7, memory2, instance3);
                }
                case 132: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_132(n6, n7, memory2, instance3);
                }
                case 133: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_133(n6, n7, memory2, instance3);
                }
                case 134: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_134(n6, n7, memory2, instance3);
                }
                case 135: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_135(n6, n7, memory2, instance3);
                }
                case 138: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_138(n6, n7, memory2, instance3);
                }
                case 157: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_157(n6, n7, memory2, instance3);
                }
                case 158: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_158(n6, n7, memory2, instance3);
                }
                case 159: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_159(n6, n7, memory2, instance3);
                }
                case 160: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_160(n6, n7, memory2, instance3);
                }
                case 163: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_163(n6, n7, memory2, instance3);
                }
                case 164: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_164(n6, n7, memory2, instance3);
                }
                case 181: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_181(n6, n7, memory2, instance3);
                }
                case 187: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_187(n6, n7, memory2, instance3);
                }
                case 189: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_189(n6, n7, memory2, instance3);
                }
                case 194: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_194(n6, n7, memory2, instance3);
                }
                case 227: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_227(n6, n7, memory2, instance3);
                }
                case 229: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_229(n6, n7, memory2, instance3);
                }
                case 233: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_233(n6, n7, memory2, instance3);
                }
                case 239: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_239(n6, n7, memory2, instance3);
                }
                case 250: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_250(n6, n7, memory2, instance3);
                }
                case 251: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_251(n6, n7, memory2, instance3);
                }
                case 261: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_261(n6, n7, memory2, instance3);
                }
                case 267: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_267(n6, n7, memory2, instance3);
                }
                case 269: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_269(n6, n7, memory2, instance3);
                }
                case 271: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_271(n6, n7, memory2, instance3);
                }
                case 272: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_272(n6, n7, memory2, instance3);
                }
                case 274: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_274(n6, n7, memory2, instance3);
                }
                case 278: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_278(n6, n7, memory2, instance3);
                }
                case 281: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_281(n6, n7, memory2, instance3);
                }
                case 282: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_282(n6, n7, memory2, instance3);
                }
                case 283: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_283(n6, n7, memory2, instance3);
                }
                case 284: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_284(n6, n7, memory2, instance3);
                }
                case 285: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_285(n6, n7, memory2, instance3);
                }
                case 291: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_291(n6, n7, memory2, instance3);
                }
                case 292: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_292(n6, n7, memory2, instance3);
                }
                case 294: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_294(n6, n7, memory2, instance3);
                }
                case 295: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_295(n6, n7, memory2, instance3);
                }
                case 296: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_296(n6, n7, memory2, instance3);
                }
                case 299: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_299(n6, n7, memory2, instance3);
                }
                case 300: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_300(n6, n7, memory2, instance3);
                }
                case 301: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_301(n6, n7, memory2, instance3);
                }
                case 303: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_303(n6, n7, memory2, instance3);
                }
                case 305: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_305(n6, n7, memory2, instance3);
                }
                case 310: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_310(n6, n7, memory2, instance3);
                }
                case 313: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_313(n6, n7, memory2, instance3);
                }
                case 315: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_315(n6, n7, memory2, instance3);
                }
                case 316: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_316(n6, n7, memory2, instance3);
                }
                case 319: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_319(n6, n7, memory2, instance3);
                }
                case 322: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_322(n6, n7, memory2, instance3);
                }
                case 325: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_325(n6, n7, memory2, instance3);
                }
                case 326: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_326(n6, n7, memory2, instance3);
                }
                case 333: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_333(n6, n7, memory2, instance3);
                }
                case 353: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_353(n6, n7, memory2, instance3);
                }
                case 371: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_371(n6, n7, memory2, instance3);
                }
                case 384: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_384(n6, n7, memory2, instance3);
                }
                case 386: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_386(n6, n7, memory2, instance3);
                }
                case 396: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_396(n6, n7, memory2, instance3);
                }
                case 398: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_398(n6, n7, memory2, instance3);
                }
                case 399: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_399(n6, n7, memory2, instance3);
                }
                case 415: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_415(n6, n7, memory2, instance3);
                }
                case 418: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_418(n6, n7, memory2, instance3);
                }
                case 423: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_423(n6, n7, memory2, instance3);
                }
                case 439: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_439(n6, n7, memory2, instance3);
                }
                case 445: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_445(n6, n7, memory2, instance3);
                }
                case 446: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_446(n6, n7, memory2, instance3);
                }
                case 447: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_447(n6, n7, memory2, instance3);
                }
                case 448: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_448(n6, n7, memory2, instance3);
                }
                case 450: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_450(n6, n7, memory2, instance3);
                }
                case 453: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_453(n6, n7, memory2, instance3);
                }
                case 454: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_454(n6, n7, memory2, instance3);
                }
                case 461: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_461(n6, n7, memory2, instance3);
                }
                case 462: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_462(n6, n7, memory2, instance3);
                }
                case 463: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_463(n6, n7, memory2, instance3);
                }
                case 464: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_464(n6, n7, memory2, instance3);
                }
                case 465: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_465(n6, n7, memory2, instance3);
                }
                case 466: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_466(n6, n7, memory2, instance3);
                }
                case 467: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_467(n6, n7, memory2, instance3);
                }
                case 468: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_468(n6, n7, memory2, instance3);
                }
                case 469: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_469(n6, n7, memory2, instance3);
                }
                case 470: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_470(n6, n7, memory2, instance3);
                }
                case 471: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_471(n6, n7, memory2, instance3);
                }
                case 472: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_472(n6, n7, memory2, instance3);
                }
                case 473: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_473(n6, n7, memory2, instance3);
                }
                case 474: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_474(n6, n7, memory2, instance3);
                }
                case 475: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_475(n6, n7, memory2, instance3);
                }
                case 476: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_476(n6, n7, memory2, instance3);
                }
                case 477: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_477(n6, n7, memory2, instance3);
                }
                case 478: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_478(n6, n7, memory2, instance3);
                }
                case 479: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_479(n6, n7, memory2, instance3);
                }
                case 480: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_480(n6, n7, memory2, instance3);
                }
                case 481: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_481(n6, n7, memory2, instance3);
                }
                case 482: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_482(n6, n7, memory2, instance3);
                }
                case 483: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_483(n6, n7, memory2, instance3);
                }
                case 484: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_484(n6, n7, memory2, instance3);
                }
                case 485: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_485(n6, n7, memory2, instance3);
                }
                case 486: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_486(n6, n7, memory2, instance3);
                }
                case 487: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_487(n6, n7, memory2, instance3);
                }
                case 488: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_488(n6, n7, memory2, instance3);
                }
                case 489: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_489(n6, n7, memory2, instance3);
                }
                case 490: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_490(n6, n7, memory2, instance3);
                }
                case 491: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_491(n6, n7, memory2, instance3);
                }
                case 492: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_492(n6, n7, memory2, instance3);
                }
                case 493: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_493(n6, n7, memory2, instance3);
                }
                case 494: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_494(n6, n7, memory2, instance3);
                }
                case 495: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_495(n6, n7, memory2, instance3);
                }
                case 496: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_496(n6, n7, memory2, instance3);
                }
                case 497: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_497(n6, n7, memory2, instance3);
                }
                case 498: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_498(n6, n7, memory2, instance3);
                }
                case 499: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_499(n6, n7, memory2, instance3);
                }
                case 500: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_500(n6, n7, memory2, instance3);
                }
                case 501: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_501(n6, n7, memory2, instance3);
                }
                case 502: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_502(n6, n7, memory2, instance3);
                }
                case 503: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_503(n6, n7, memory2, instance3);
                }
                case 504: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_504(n6, n7, memory2, instance3);
                }
                case 505: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_505(n6, n7, memory2, instance3);
                }
                case 506: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_506(n6, n7, memory2, instance3);
                }
                case 507: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_507(n6, n7, memory2, instance3);
                }
                case 508: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_508(n6, n7, memory2, instance3);
                }
                case 509: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_509(n6, n7, memory2, instance3);
                }
                case 510: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_510(n6, n7, memory2, instance3);
                }
                case 511: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_511(n6, n7, memory2, instance3);
                }
                case 512: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_512(n6, n7, memory2, instance3);
                }
                case 513: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_513(n6, n7, memory2, instance3);
                }
                case 515: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_515(n6, n7, memory2, instance3);
                }
                case 516: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_516(n6, n7, memory2, instance3);
                }
                case 517: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_517(n6, n7, memory2, instance3);
                }
                case 518: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_518(n6, n7, memory2, instance3);
                }
                case 519: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_519(n6, n7, memory2, instance3);
                }
                case 520: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_520(n6, n7, memory2, instance3);
                }
                case 521: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_521(n6, n7, memory2, instance3);
                }
                case 522: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_522(n6, n7, memory2, instance3);
                }
                case 523: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_523(n6, n7, memory2, instance3);
                }
                case 524: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_524(n6, n7, memory2, instance3);
                }
                case 525: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_525(n6, n7, memory2, instance3);
                }
                case 526: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_526(n6, n7, memory2, instance3);
                }
                case 527: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_527(n6, n7, memory2, instance3);
                }
                case 528: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_528(n6, n7, memory2, instance3);
                }
                case 529: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_529(n6, n7, memory2, instance3);
                }
                case 530: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_530(n6, n7, memory2, instance3);
                }
                case 531: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_531(n6, n7, memory2, instance3);
                }
                case 532: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_532(n6, n7, memory2, instance3);
                }
                case 533: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_533(n6, n7, memory2, instance3);
                }
                case 546: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_546(n6, n7, memory2, instance3);
                }
                case 570: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_570(n6, n7, memory2, instance3);
                }
                case 571: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_571(n6, n7, memory2, instance3);
                }
                case 575: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_575(n6, n7, memory2, instance3);
                }
                case 577: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_577(n6, n7, memory2, instance3);
                }
                case 595: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_595(n6, n7, memory2, instance3);
                }
                case 596: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_596(n6, n7, memory2, instance3);
                }
                case 597: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_597(n6, n7, memory2, instance3);
                }
                case 599: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_599(n6, n7, memory2, instance3);
                }
                case 601: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_601(n6, n7, memory2, instance3);
                }
                case 602: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_602(n6, n7, memory2, instance3);
                }
                case 604: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_604(n6, n7, memory2, instance3);
                }
                case 605: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_605(n6, n7, memory2, instance3);
                }
                case 607: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_607(n6, n7, memory2, instance3);
                }
                case 615: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_615(n6, n7, memory2, instance3);
                }
                case 623: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_623(n6, n7, memory2, instance3);
                }
                case 644: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_644(n6, n7, memory2, instance3);
                }
                case 645: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_645(n6, n7, memory2, instance3);
                }
                case 647: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_647(n6, n7, memory2, instance3);
                }
                case 648: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_648(n6, n7, memory2, instance3);
                }
                case 650: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_650(n6, n7, memory2, instance3);
                }
                case 651: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_651(n6, n7, memory2, instance3);
                }
                case 654: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_654(n6, n7, memory2, instance3);
                }
                case 655: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_655(n6, n7, memory2, instance3);
                }
                case 657: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_657(n6, n7, memory2, instance3);
                }
                case 658: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_658(n6, n7, memory2, instance3);
                }
                case 660: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_660(n6, n7, memory2, instance3);
                }
                case 661: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_661(n6, n7, memory2, instance3);
                }
                case 662: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_662(n6, n7, memory2, instance3);
                }
                case 663: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_663(n6, n7, memory2, instance3);
                }
                case 664: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_664(n6, n7, memory2, instance3);
                }
                case 666: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_666(n6, n7, memory2, instance3);
                }
                case 667: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_667(n6, n7, memory2, instance3);
                }
                case 669: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_669(n6, n7, memory2, instance3);
                }
                case 670: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_670(n6, n7, memory2, instance3);
                }
                case 672: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_672(n6, n7, memory2, instance3);
                }
                case 673: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_673(n6, n7, memory2, instance3);
                }
                case 674: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_674(n6, n7, memory2, instance3);
                }
                case 675: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_675(n6, n7, memory2, instance3);
                }
                case 682: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_682(n6, n7, memory2, instance3);
                }
                case 684: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_684(n6, n7, memory2, instance3);
                }
                case 685: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_685(n6, n7, memory2, instance3);
                }
                case 688: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_688(n6, n7, memory2, instance3);
                }
                case 690: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_690(n6, n7, memory2, instance3);
                }
                case 691: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_691(n6, n7, memory2, instance3);
                }
                case 692: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_692(n6, n7, memory2, instance3);
                }
                case 696: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_696(n6, n7, memory2, instance3);
                }
                case 697: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_697(n6, n7, memory2, instance3);
                }
                case 699: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_699(n6, n7, memory2, instance3);
                }
                case 700: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_700(n6, n7, memory2, instance3);
                }
                case 701: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_701(n6, n7, memory2, instance3);
                }
                case 703: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_703(n6, n7, memory2, instance3);
                }
                case 704: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_704(n6, n7, memory2, instance3);
                }
                case 705: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_705(n6, n7, memory2, instance3);
                }
                case 707: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_707(n6, n7, memory2, instance3);
                }
                case 709: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_709(n6, n7, memory2, instance3);
                }
                case 711: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_711(n6, n7, memory2, instance3);
                }
                case 712: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_712(n6, n7, memory2, instance3);
                }
                case 713: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_713(n6, n7, memory2, instance3);
                }
                case 714: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_714(n6, n7, memory2, instance3);
                }
                case 715: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_715(n6, n7, memory2, instance3);
                }
                case 716: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_716(n6, n7, memory2, instance3);
                }
                case 717: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_717(n6, n7, memory2, instance3);
                }
                case 720: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_720(n6, n7, memory2, instance3);
                }
                case 721: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_721(n6, n7, memory2, instance3);
                }
                case 727: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_727(n6, n7, memory2, instance3);
                }
                case 730: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_730(n6, n7, memory2, instance3);
                }
                case 731: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_731(n6, n7, memory2, instance3);
                }
                case 735: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_735(n6, n7, memory2, instance3);
                }
                case 736: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_736(n6, n7, memory2, instance3);
                }
                case 737: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_737(n6, n7, memory2, instance3);
                }
                case 738: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_738(n6, n7, memory2, instance3);
                }
                case 742: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_742(n6, n7, memory2, instance3);
                }
                case 744: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_744(n6, n7, memory2, instance3);
                }
                case 745: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_745(n6, n7, memory2, instance3);
                }
                case 747: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_747(n6, n7, memory2, instance3);
                }
                case 748: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_748(n6, n7, memory2, instance3);
                }
                case 749: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_749(n6, n7, memory2, instance3);
                }
                case 751: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_751(n6, n7, memory2, instance3);
                }
                case 752: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_752(n6, n7, memory2, instance3);
                }
                case 754: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_754(n6, n7, memory2, instance3);
                }
                case 755: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_755(n6, n7, memory2, instance3);
                }
                case 756: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_756(n6, n7, memory2, instance3);
                }
                case 758: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_758(n6, n7, memory2, instance3);
                }
                case 760: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_760(n6, n7, memory2, instance3);
                }
                case 762: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_762(n6, n7, memory2, instance3);
                }
                case 763: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_763(n6, n7, memory2, instance3);
                }
                case 764: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_764(n6, n7, memory2, instance3);
                }
                case 765: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_765(n6, n7, memory2, instance3);
                }
                case 767: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_767(n6, n7, memory2, instance3);
                }
                case 768: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_768(n6, n7, memory2, instance3);
                }
                case 770: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_770(n6, n7, memory2, instance3);
                }
                case 771: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_771(n6, n7, memory2, instance3);
                }
                case 772: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_772(n6, n7, memory2, instance3);
                }
                case 773: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_773(n6, n7, memory2, instance3);
                }
                case 774: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_774(n6, n7, memory2, instance3);
                }
                case 776: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_776(n6, n7, memory2, instance3);
                }
                case 778: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_778(n6, n7, memory2, instance3);
                }
                case 779: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_779(n6, n7, memory2, instance3);
                }
                case 782: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_782(n6, n7, memory2, instance3);
                }
                case 785: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_785(n6, n7, memory2, instance3);
                }
                case 787: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_787(n6, n7, memory2, instance3);
                }
                case 789: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_789(n6, n7, memory2, instance3);
                }
                case 794: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_794(n6, n7, memory2, instance3);
                }
                case 796: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_796(n6, n7, memory2, instance3);
                }
                case 797: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_797(n6, n7, memory2, instance3);
                }
                case 798: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_798(n6, n7, memory2, instance3);
                }
                case 810: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_810(n6, n7, memory2, instance3);
                }
                case 819: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_819(n6, n7, memory2, instance3);
                }
                case 821: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_821(n6, n7, memory2, instance3);
                }
                case 823: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_823(n6, n7, memory2, instance3);
                }
                case 824: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_824(n6, n7, memory2, instance3);
                }
                case 825: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_825(n6, n7, memory2, instance3);
                }
                case 842: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_842(n6, n7, memory2, instance3);
                }
                case 845: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_845(n6, n7, memory2, instance3);
                }
                case 850: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_850(n6, n7, memory2, instance3);
                }
                case 851: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_851(n6, n7, memory2, instance3);
                }
                case 858: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_858(n6, n7, memory2, instance3);
                }
                case 859: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_859(n6, n7, memory2, instance3);
                }
                case 868: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_868(n6, n7, memory2, instance3);
                }
                case 869: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_869(n6, n7, memory2, instance3);
                }
                case 872: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_872(n6, n7, memory2, instance3);
                }
                case 873: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_873(n6, n7, memory2, instance3);
                }
                case 876: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_876(n6, n7, memory2, instance3);
                }
                case 877: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_877(n6, n7, memory2, instance3);
                }
                case 880: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_880(n6, n7, memory2, instance3);
                }
                case 881: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_881(n6, n7, memory2, instance3);
                }
                case 883: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_883(n6, n7, memory2, instance3);
                }
                case 885: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_885(n6, n7, memory2, instance3);
                }
                case 886: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_886(n6, n7, memory2, instance3);
                }
                case 888: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_888(n6, n7, memory2, instance3);
                }
                case 889: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_889(n6, n7, memory2, instance3);
                }
                case 892: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_892(n6, n7, memory2, instance3);
                }
                case 893: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_893(n6, n7, memory2, instance3);
                }
                case 895: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_895(n6, n7, memory2, instance3);
                }
                case 896: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_896(n6, n7, memory2, instance3);
                }
                case 898: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_898(n6, n7, memory2, instance3);
                }
                case 902: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_902(n6, n7, memory2, instance3);
                }
                case 904: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_904(n6, n7, memory2, instance3);
                }
                case 905: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_905(n6, n7, memory2, instance3);
                }
                case 911: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_911(n6, n7, memory2, instance3);
                }
                case 913: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_913(n6, n7, memory2, instance3);
                }
                case 914: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_914(n6, n7, memory2, instance3);
                }
                case 915: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_915(n6, n7, memory2, instance3);
                }
                case 921: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_921(n6, n7, memory2, instance3);
                }
                case 923: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_923(n6, n7, memory2, instance3);
                }
                case 924: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_924(n6, n7, memory2, instance3);
                }
                case 926: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_926(n6, n7, memory2, instance3);
                }
                case 927: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_927(n6, n7, memory2, instance3);
                }
                case 930: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_930(n6, n7, memory2, instance3);
                }
                case 933: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_933(n6, n7, memory2, instance3);
                }
                case 934: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_934(n6, n7, memory2, instance3);
                }
                case 936: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_936(n6, n7, memory2, instance3);
                }
                case 937: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_937(n6, n7, memory2, instance3);
                }
                case 938: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_938(n6, n7, memory2, instance3);
                }
                case 942: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_942(n6, n7, memory2, instance3);
                }
                case 944: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_944(n6, n7, memory2, instance3);
                }
                case 945: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_945(n6, n7, memory2, instance3);
                }
                case 946: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_946(n6, n7, memory2, instance3);
                }
                case 947: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_947(n6, n7, memory2, instance3);
                }
                case 949: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_949(n6, n7, memory2, instance3);
                }
                case 951: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_951(n6, n7, memory2, instance3);
                }
                case 952: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_952(n6, n7, memory2, instance3);
                }
                case 953: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_953(n6, n7, memory2, instance3);
                }
                case 954: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_954(n6, n7, memory2, instance3);
                }
                case 956: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_956(n6, n7, memory2, instance3);
                }
                case 957: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_957(n6, n7, memory2, instance3);
                }
                case 958: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_958(n6, n7, memory2, instance3);
                }
                case 960: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_960(n6, n7, memory2, instance3);
                }
                case 962: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_962(n6, n7, memory2, instance3);
                }
                case 964: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_964(n6, n7, memory2, instance3);
                }
                case 965: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_965(n6, n7, memory2, instance3);
                }
                case 966: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_966(n6, n7, memory2, instance3);
                }
                case 967: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_967(n6, n7, memory2, instance3);
                }
                case 968: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_968(n6, n7, memory2, instance3);
                }
                case 969: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_969(n6, n7, memory2, instance3);
                }
                case 970: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_970(n6, n7, memory2, instance3);
                }
                case 973: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_973(n6, n7, memory2, instance3);
                }
                case 975: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_975(n6, n7, memory2, instance3);
                }
                case 980: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_980(n6, n7, memory2, instance3);
                }
                case 982: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_982(n6, n7, memory2, instance3);
                }
                case 985: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_985(n6, n7, memory2, instance3);
                }
                case 986: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_986(n6, n7, memory2, instance3);
                }
                case 988: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_988(n6, n7, memory2, instance3);
                }
                case 996: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_996(n6, n7, memory2, instance3);
                }
                case 997: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_997(n6, n7, memory2, instance3);
                }
                case 999: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_999(n6, n7, memory2, instance3);
                }
                case 1000: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1000(n6, n7, memory2, instance3);
                }
                case 1007: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1007(n6, n7, memory2, instance3);
                }
                case 1009: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1009(n6, n7, memory2, instance3);
                }
                case 1010: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1010(n6, n7, memory2, instance3);
                }
                case 1012: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1012(n6, n7, memory2, instance3);
                }
                case 1013: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1013(n6, n7, memory2, instance3);
                }
                case 1015: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1015(n6, n7, memory2, instance3);
                }
                case 1017: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1017(n6, n7, memory2, instance3);
                }
                case 1018: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1018(n6, n7, memory2, instance3);
                }
                case 1020: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1020(n6, n7, memory2, instance3);
                }
                case 1022: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1022(n6, n7, memory2, instance3);
                }
                case 1025: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1025(n6, n7, memory2, instance3);
                }
                case 1029: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1029(n6, n7, memory2, instance3);
                }
                case 1034: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1034(n6, n7, memory2, instance3);
                }
                case 1039: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1039(n6, n7, memory2, instance3);
                }
                case 1043: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1043(n6, n7, memory2, instance3);
                }
                case 1045: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1045(n6, n7, memory2, instance3);
                }
                case 1046: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1046(n6, n7, memory2, instance3);
                }
                case 1049: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1049(n6, n7, memory2, instance3);
                }
                case 1050: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1050(n6, n7, memory2, instance3);
                }
                case 1052: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1052(n6, n7, memory2, instance3);
                }
                case 1053: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1053(n6, n7, memory2, instance3);
                }
                case 1056: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1056(n6, n7, memory2, instance3);
                }
                case 1060: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1060(n6, n7, memory2, instance3);
                }
                case 1061: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1061(n6, n7, memory2, instance3);
                }
                case 1068: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1068(n6, n7, memory2, instance3);
                }
                case 1070: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1070(n6, n7, memory2, instance3);
                }
                case 1072: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1072(n6, n7, memory2, instance3);
                }
                case 1076: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1076(n6, n7, memory2, instance3);
                }
                case 1077: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1077(n6, n7, memory2, instance3);
                }
                case 1081: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1081(n6, n7, memory2, instance3);
                }
                case 1093: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1093(n6, n7, memory2, instance3);
                }
                case 1097: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1097(n6, n7, memory2, instance3);
                }
                case 1098: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1098(n6, n7, memory2, instance3);
                }
                case 1104: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1104(n6, n7, memory2, instance3);
                }
                case 1106: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1106(n6, n7, memory2, instance3);
                }
                case 1107: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1107(n6, n7, memory2, instance3);
                }
                case 1109: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1109(n6, n7, memory2, instance3);
                }
                case 1110: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1110(n6, n7, memory2, instance3);
                }
                case 1111: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1111(n6, n7, memory2, instance3);
                }
                case 1112: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1112(n6, n7, memory2, instance3);
                }
                case 1113: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1113(n6, n7, memory2, instance3);
                }
                case 1114: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1114(n6, n7, memory2, instance3);
                }
                case 1115: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1115(n6, n7, memory2, instance3);
                }
                case 1116: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1116(n6, n7, memory2, instance3);
                }
                case 1117: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1117(n6, n7, memory2, instance3);
                }
                case 1118: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1118(n6, n7, memory2, instance3);
                }
                case 1119: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1119(n6, n7, memory2, instance3);
                }
                case 1120: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1120(n6, n7, memory2, instance3);
                }
                case 1121: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1121(n6, n7, memory2, instance3);
                }
                case 1122: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1122(n6, n7, memory2, instance3);
                }
                case 1123: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1123(n6, n7, memory2, instance3);
                }
                case 1124: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1124(n6, n7, memory2, instance3);
                }
                case 1125: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1125(n6, n7, memory2, instance3);
                }
                case 1126: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1126(n6, n7, memory2, instance3);
                }
                case 1127: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1127(n6, n7, memory2, instance3);
                }
                case 1128: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1128(n6, n7, memory2, instance3);
                }
                case 1129: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1129(n6, n7, memory2, instance3);
                }
                case 1130: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1130(n6, n7, memory2, instance3);
                }
                case 1131: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1131(n6, n7, memory2, instance3);
                }
                case 1132: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1132(n6, n7, memory2, instance3);
                }
                case 1133: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1133(n6, n7, memory2, instance3);
                }
                case 1134: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1134(n6, n7, memory2, instance3);
                }
                case 1135: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1135(n6, n7, memory2, instance3);
                }
                case 1136: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1136(n6, n7, memory2, instance3);
                }
                case 1137: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1137(n6, n7, memory2, instance3);
                }
                case 1138: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1138(n6, n7, memory2, instance3);
                }
                case 1139: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1139(n6, n7, memory2, instance3);
                }
                case 1140: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1140(n6, n7, memory2, instance3);
                }
                case 1141: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1141(n6, n7, memory2, instance3);
                }
                case 1142: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1142(n6, n7, memory2, instance3);
                }
                case 1143: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1143(n6, n7, memory2, instance3);
                }
                case 1145: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1145(n6, n7, memory2, instance3);
                }
                case 1146: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1146(n6, n7, memory2, instance3);
                }
                case 1147: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1147(n6, n7, memory2, instance3);
                }
                case 1148: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1148(n6, n7, memory2, instance3);
                }
                case 1149: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1149(n6, n7, memory2, instance3);
                }
                case 1150: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1150(n6, n7, memory2, instance3);
                }
                case 1154: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1154(n6, n7, memory2, instance3);
                }
                case 1155: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1155(n6, n7, memory2, instance3);
                }
                case 1158: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1158(n6, n7, memory2, instance3);
                }
                case 1159: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1159(n6, n7, memory2, instance3);
                }
                case 1160: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1160(n6, n7, memory2, instance3);
                }
                case 1161: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1161(n6, n7, memory2, instance3);
                }
                case 1165: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1165(n6, n7, memory2, instance3);
                }
                case 1166: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1166(n6, n7, memory2, instance3);
                }
                case 1167: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1167(n6, n7, memory2, instance3);
                }
                case 1168: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1168(n6, n7, memory2, instance3);
                }
                case 1171: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1171(n6, n7, memory2, instance3);
                }
                case 1172: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1172(n6, n7, memory2, instance3);
                }
                case 1173: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1173(n6, n7, memory2, instance3);
                }
                case 1174: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1174(n6, n7, memory2, instance3);
                }
                case 1175: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1175(n6, n7, memory2, instance3);
                }
                case 1176: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1176(n6, n7, memory2, instance3);
                }
                case 1177: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1177(n6, n7, memory2, instance3);
                }
                case 1178: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1178(n6, n7, memory2, instance3);
                }
                case 1179: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1179(n6, n7, memory2, instance3);
                }
                case 1180: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1180(n6, n7, memory2, instance3);
                }
                case 1181: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1181(n6, n7, memory2, instance3);
                }
                case 1182: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1182(n6, n7, memory2, instance3);
                }
                case 1183: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1183(n6, n7, memory2, instance3);
                }
                case 1184: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1184(n6, n7, memory2, instance3);
                }
                case 1186: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1186(n6, n7, memory2, instance3);
                }
                case 1187: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1187(n6, n7, memory2, instance3);
                }
                case 1190: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1190(n6, n7, memory2, instance3);
                }
                case 1191: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1191(n6, n7, memory2, instance3);
                }
                case 1194: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1194(n6, n7, memory2, instance3);
                }
                case 1195: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1195(n6, n7, memory2, instance3);
                }
                case 1197: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1197(n6, n7, memory2, instance3);
                }
                case 1198: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1198(n6, n7, memory2, instance3);
                }
                case 1200: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1200(n6, n7, memory2, instance3);
                }
                case 1201: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1201(n6, n7, memory2, instance3);
                }
                case 1203: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1203(n6, n7, memory2, instance3);
                }
                case 1204: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1204(n6, n7, memory2, instance3);
                }
                case 1206: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1206(n6, n7, memory2, instance3);
                }
                case 1207: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1207(n6, n7, memory2, instance3);
                }
                case 1209: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1209(n6, n7, memory2, instance3);
                }
                case 1212: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1212(n6, n7, memory2, instance3);
                }
                case 1213: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1213(n6, n7, memory2, instance3);
                }
                case 1214: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1214(n6, n7, memory2, instance3);
                }
                case 1219: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1219(n6, n7, memory2, instance3);
                }
                case 1220: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1220(n6, n7, memory2, instance3);
                }
                case 1221: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1221(n6, n7, memory2, instance3);
                }
                case 1222: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1222(n6, n7, memory2, instance3);
                }
                case 1223: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1223(n6, n7, memory2, instance3);
                }
                case 1224: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1224(n6, n7, memory2, instance3);
                }
                case 1225: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1225(n6, n7, memory2, instance3);
                }
                case 1226: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1226(n6, n7, memory2, instance3);
                }
                case 1227: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1227(n6, n7, memory2, instance3);
                }
                case 1228: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1228(n6, n7, memory2, instance3);
                }
                case 1229: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1229(n6, n7, memory2, instance3);
                }
                case 1232: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1232(n6, n7, memory2, instance3);
                }
                case 1233: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1233(n6, n7, memory2, instance3);
                }
                case 1235: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1235(n6, n7, memory2, instance3);
                }
                case 1240: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1240(n6, n7, memory2, instance3);
                }
                case 1242: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1242(n6, n7, memory2, instance3);
                }
                case 1243: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1243(n6, n7, memory2, instance3);
                }
                case 1249: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1249(n6, n7, memory2, instance3);
                }
                case 1254: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1254(n6, n7, memory2, instance3);
                }
                case 1255: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1255(n6, n7, memory2, instance3);
                }
                case 1257: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1257(n6, n7, memory2, instance3);
                }
                case 1258: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1258(n6, n7, memory2, instance3);
                }
                case 1297: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1297(n6, n7, memory2, instance3);
                }
                case 1298: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1298(n6, n7, memory2, instance3);
                }
                case 1318: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1318(n6, n7, memory2, instance3);
                }
                case 1319: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1319(n6, n7, memory2, instance3);
                }
                case 1321: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1321(n6, n7, memory2, instance3);
                }
                case 1322: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1322(n6, n7, memory2, instance3);
                }
                case 1323: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1323(n6, n7, memory2, instance3);
                }
                case 1324: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1324(n6, n7, memory2, instance3);
                }
                case 1325: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1325(n6, n7, memory2, instance3);
                }
                case 1326: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1326(n6, n7, memory2, instance3);
                }
                case 1327: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1327(n6, n7, memory2, instance3);
                }
                case 1328: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1328(n6, n7, memory2, instance3);
                }
                case 1329: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1329(n6, n7, memory2, instance3);
                }
                case 1330: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1330(n6, n7, memory2, instance3);
                }
                case 1331: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1331(n6, n7, memory2, instance3);
                }
                case 1332: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1332(n6, n7, memory2, instance3);
                }
                case 1333: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1333(n6, n7, memory2, instance3);
                }
                case 1334: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1334(n6, n7, memory2, instance3);
                }
                case 1335: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1335(n6, n7, memory2, instance3);
                }
                case 1336: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1336(n6, n7, memory2, instance3);
                }
                case 1337: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1337(n6, n7, memory2, instance3);
                }
                case 1338: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1338(n6, n7, memory2, instance3);
                }
                case 1339: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1339(n6, n7, memory2, instance3);
                }
                case 1340: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1340(n6, n7, memory2, instance3);
                }
                case 1341: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1341(n6, n7, memory2, instance3);
                }
                case 1342: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1342(n6, n7, memory2, instance3);
                }
                case 1343: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1343(n6, n7, memory2, instance3);
                }
                case 1344: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1344(n6, n7, memory2, instance3);
                }
                case 1345: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1345(n6, n7, memory2, instance3);
                }
                case 1346: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1346(n6, n7, memory2, instance3);
                }
                case 1347: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1347(n6, n7, memory2, instance3);
                }
                case 1348: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1348(n6, n7, memory2, instance3);
                }
                case 1349: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1349(n6, n7, memory2, instance3);
                }
                case 1350: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1350(n6, n7, memory2, instance3);
                }
                case 1351: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1351(n6, n7, memory2, instance3);
                }
                case 1352: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1352(n6, n7, memory2, instance3);
                }
                case 1353: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1353(n6, n7, memory2, instance3);
                }
                case 1354: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1354(n6, n7, memory2, instance3);
                }
                case 1355: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1355(n6, n7, memory2, instance3);
                }
                case 1356: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1356(n6, n7, memory2, instance3);
                }
                case 1357: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1357(n6, n7, memory2, instance3);
                }
                case 1358: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1358(n6, n7, memory2, instance3);
                }
                case 1359: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1359(n6, n7, memory2, instance3);
                }
                case 1360: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1360(n6, n7, memory2, instance3);
                }
                case 1361: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1361(n6, n7, memory2, instance3);
                }
                case 1362: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1362(n6, n7, memory2, instance3);
                }
                case 1363: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1363(n6, n7, memory2, instance3);
                }
                case 1364: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1364(n6, n7, memory2, instance3);
                }
                case 1365: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1365(n6, n7, memory2, instance3);
                }
                case 1366: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1366(n6, n7, memory2, instance3);
                }
                case 1367: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1367(n6, n7, memory2, instance3);
                }
                case 1368: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1368(n6, n7, memory2, instance3);
                }
                case 1369: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1369(n6, n7, memory2, instance3);
                }
                case 1370: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1370(n6, n7, memory2, instance3);
                }
                case 1371: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1371(n6, n7, memory2, instance3);
                }
                case 1373: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1373(n6, n7, memory2, instance3);
                }
                case 1374: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1374(n6, n7, memory2, instance3);
                }
                case 1375: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1375(n6, n7, memory2, instance3);
                }
                case 1376: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1376(n6, n7, memory2, instance3);
                }
                case 1377: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1377(n6, n7, memory2, instance3);
                }
                case 1378: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1378(n6, n7, memory2, instance3);
                }
                case 1379: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1379(n6, n7, memory2, instance3);
                }
                case 1380: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1380(n6, n7, memory2, instance3);
                }
                case 1381: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1381(n6, n7, memory2, instance3);
                }
                case 1382: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1382(n6, n7, memory2, instance3);
                }
                case 1383: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1383(n6, n7, memory2, instance3);
                }
                case 1384: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1384(n6, n7, memory2, instance3);
                }
                case 1385: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1385(n6, n7, memory2, instance3);
                }
                case 1386: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1386(n6, n7, memory2, instance3);
                }
                case 1387: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1387(n6, n7, memory2, instance3);
                }
                case 1388: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1388(n6, n7, memory2, instance3);
                }
                case 1389: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1389(n6, n7, memory2, instance3);
                }
                case 1390: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1390(n6, n7, memory2, instance3);
                }
                case 1391: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1391(n6, n7, memory2, instance3);
                }
                case 1407: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1407(n6, n7, memory2, instance3);
                }
                case 1578: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1578(n6, n7, memory2, instance3);
                }
                case 1581: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1581(n6, n7, memory2, instance3);
                }
                case 1583: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1583(n6, n7, memory2, instance3);
                }
                case 1586: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1586(n6, n7, memory2, instance3);
                }
                case 1587: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1587(n6, n7, memory2, instance3);
                }
                case 1592: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1592(n6, n7, memory2, instance3);
                }
                case 1603: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1603(n6, n7, memory2, instance3);
                }
                case 1613: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1613(n6, n7, memory2, instance3);
                }
                case 1635: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1635(n6, n7, memory2, instance3);
                }
                case 1650: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1650(n6, n7, memory2, instance3);
                }
                case 1651: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1651(n6, n7, memory2, instance3);
                }
                case 1657: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1657(n6, n7, memory2, instance3);
                }
                case 1662: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1662(n6, n7, memory2, instance3);
                }
                case 1680: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1680(n6, n7, memory2, instance3);
                }
                case 1682: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1682(n6, n7, memory2, instance3);
                }
                case 1692: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1692(n6, n7, memory2, instance3);
                }
                case 1693: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1693(n6, n7, memory2, instance3);
                }
                case 1699: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1699(n6, n7, memory2, instance3);
                }
                case 1700: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1700(n6, n7, memory2, instance3);
                }
                case 1706: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1706(n6, n7, memory2, instance3);
                }
                case 1707: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1707(n6, n7, memory2, instance3);
                }
                case 1724: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1724(n6, n7, memory2, instance3);
                }
                case 1726: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1726(n6, n7, memory2, instance3);
                }
                case 1727: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1727(n6, n7, memory2, instance3);
                }
                case 1728: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1728(n6, n7, memory2, instance3);
                }
                case 1729: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1729(n6, n7, memory2, instance3);
                }
                case 1730: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1730(n6, n7, memory2, instance3);
                }
                case 1731: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1731(n6, n7, memory2, instance3);
                }
                case 1732: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1732(n6, n7, memory2, instance3);
                }
                case 1733: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1733(n6, n7, memory2, instance3);
                }
                case 1734: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1734(n6, n7, memory2, instance3);
                }
                case 1735: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1735(n6, n7, memory2, instance3);
                }
                case 1736: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1736(n6, n7, memory2, instance3);
                }
                case 1738: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1738(n6, n7, memory2, instance3);
                }
                case 1739: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1739(n6, n7, memory2, instance3);
                }
                case 1754: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1754(n6, n7, memory2, instance3);
                }
                case 1762: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1762(n6, n7, memory2, instance3);
                }
                case 1774: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1774(n6, n7, memory2, instance3);
                }
                case 1778: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1778(n6, n7, memory2, instance3);
                }
                case 1779: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1779(n6, n7, memory2, instance3);
                }
                case 1786: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1786(n6, n7, memory2, instance3);
                }
                case 1788: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1788(n6, n7, memory2, instance3);
                }
                case 1789: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1789(n6, n7, memory2, instance3);
                }
                case 1792: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1792(n6, n7, memory2, instance3);
                }
                case 1793: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1793(n6, n7, memory2, instance3);
                }
                case 1794: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1794(n6, n7, memory2, instance3);
                }
                case 1795: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1795(n6, n7, memory2, instance3);
                }
                case 1797: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1797(n6, n7, memory2, instance3);
                }
                case 1798: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1798(n6, n7, memory2, instance3);
                }
                case 1799: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1799(n6, n7, memory2, instance3);
                }
                case 1812: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1812(n6, n7, memory2, instance3);
                }
                case 1813: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1813(n6, n7, memory2, instance3);
                }
                case 1814: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1814(n6, n7, memory2, instance3);
                }
                case 1828: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1828(n6, n7, memory2, instance3);
                }
                case 1829: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1829(n6, n7, memory2, instance3);
                }
                case 1845: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1845(n6, n7, memory2, instance3);
                }
                case 1846: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1846(n6, n7, memory2, instance3);
                }
                case 1848: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1848(n6, n7, memory2, instance3);
                }
                case 1860: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1860(n6, n7, memory2, instance3);
                }
                case 1861: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1861(n6, n7, memory2, instance3);
                }
                case 1863: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1863(n6, n7, memory2, instance3);
                }
                case 1868: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1868(n6, n7, memory2, instance3);
                }
                case 1893: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1893(n6, n7, memory2, instance3);
                }
                case 1894: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1894(n6, n7, memory2, instance3);
                }
                case 1895: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1895(n6, n7, memory2, instance3);
                }
                case 1899: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1899(n6, n7, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2}, 5, n5, instance2)[0];
    }

    public static int call_indirect_6(int n, int n2, int n3, int n4, int n5, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n5);
        int n6 = tableInstance.requiredRef(n4);
        Instance instance2 = tableInstance.instance(n4);
        if (instance2 == null || instance2 == instance) {
            int n7 = n;
            int n8 = n2;
            int n9 = n3;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n6) {
                case 8: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_8(n7, n8, n9, memory2, instance3);
                }
                case 40: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_40(n7, n8, n9, memory2, instance3);
                }
                case 83: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_83(n7, n8, n9, memory2, instance3);
                }
                case 86: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_86(n7, n8, n9, memory2, instance3);
                }
                case 155: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_155(n7, n8, n9, memory2, instance3);
                }
                case 156: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_156(n7, n8, n9, memory2, instance3);
                }
                case 165: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_165(n7, n8, n9, memory2, instance3);
                }
                case 172: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_172(n7, n8, n9, memory2, instance3);
                }
                case 177: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_177(n7, n8, n9, memory2, instance3);
                }
                case 178: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_178(n7, n8, n9, memory2, instance3);
                }
                case 179: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_179(n7, n8, n9, memory2, instance3);
                }
                case 180: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_180(n7, n8, n9, memory2, instance3);
                }
                case 183: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_183(n7, n8, n9, memory2, instance3);
                }
                case 184: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_184(n7, n8, n9, memory2, instance3);
                }
                case 185: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_185(n7, n8, n9, memory2, instance3);
                }
                case 188: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_188(n7, n8, n9, memory2, instance3);
                }
                case 190: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_190(n7, n8, n9, memory2, instance3);
                }
                case 198: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_198(n7, n8, n9, memory2, instance3);
                }
                case 247: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_247(n7, n8, n9, memory2, instance3);
                }
                case 255: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_255(n7, n8, n9, memory2, instance3);
                }
                case 257: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_257(n7, n8, n9, memory2, instance3);
                }
                case 262: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_262(n7, n8, n9, memory2, instance3);
                }
                case 263: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_263(n7, n8, n9, memory2, instance3);
                }
                case 264: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_264(n7, n8, n9, memory2, instance3);
                }
                case 265: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_265(n7, n8, n9, memory2, instance3);
                }
                case 266: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_266(n7, n8, n9, memory2, instance3);
                }
                case 268: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_268(n7, n8, n9, memory2, instance3);
                }
                case 270: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_270(n7, n8, n9, memory2, instance3);
                }
                case 276: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_276(n7, n8, n9, memory2, instance3);
                }
                case 279: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_279(n7, n8, n9, memory2, instance3);
                }
                case 280: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_280(n7, n8, n9, memory2, instance3);
                }
                case 290: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_290(n7, n8, n9, memory2, instance3);
                }
                case 293: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_293(n7, n8, n9, memory2, instance3);
                }
                case 297: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_297(n7, n8, n9, memory2, instance3);
                }
                case 298: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_298(n7, n8, n9, memory2, instance3);
                }
                case 302: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_302(n7, n8, n9, memory2, instance3);
                }
                case 304: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_304(n7, n8, n9, memory2, instance3);
                }
                case 306: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_306(n7, n8, n9, memory2, instance3);
                }
                case 307: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_307(n7, n8, n9, memory2, instance3);
                }
                case 308: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_308(n7, n8, n9, memory2, instance3);
                }
                case 309: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_309(n7, n8, n9, memory2, instance3);
                }
                case 311: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_311(n7, n8, n9, memory2, instance3);
                }
                case 312: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_312(n7, n8, n9, memory2, instance3);
                }
                case 320: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_320(n7, n8, n9, memory2, instance3);
                }
                case 321: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_321(n7, n8, n9, memory2, instance3);
                }
                case 323: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_323(n7, n8, n9, memory2, instance3);
                }
                case 324: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_324(n7, n8, n9, memory2, instance3);
                }
                case 330: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_330(n7, n8, n9, memory2, instance3);
                }
                case 335: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_335(n7, n8, n9, memory2, instance3);
                }
                case 340: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_340(n7, n8, n9, memory2, instance3);
                }
                case 341: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_341(n7, n8, n9, memory2, instance3);
                }
                case 342: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_342(n7, n8, n9, memory2, instance3);
                }
                case 350: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_350(n7, n8, n9, memory2, instance3);
                }
                case 354: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_354(n7, n8, n9, memory2, instance3);
                }
                case 355: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_355(n7, n8, n9, memory2, instance3);
                }
                case 357: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_357(n7, n8, n9, memory2, instance3);
                }
                case 359: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_359(n7, n8, n9, memory2, instance3);
                }
                case 361: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_361(n7, n8, n9, memory2, instance3);
                }
                case 365: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_365(n7, n8, n9, memory2, instance3);
                }
                case 368: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_368(n7, n8, n9, memory2, instance3);
                }
                case 370: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_370(n7, n8, n9, memory2, instance3);
                }
                case 373: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_373(n7, n8, n9, memory2, instance3);
                }
                case 380: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_380(n7, n8, n9, memory2, instance3);
                }
                case 381: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_381(n7, n8, n9, memory2, instance3);
                }
                case 382: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_382(n7, n8, n9, memory2, instance3);
                }
                case 383: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_383(n7, n8, n9, memory2, instance3);
                }
                case 385: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_385(n7, n8, n9, memory2, instance3);
                }
                case 387: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_387(n7, n8, n9, memory2, instance3);
                }
                case 389: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_389(n7, n8, n9, memory2, instance3);
                }
                case 391: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_391(n7, n8, n9, memory2, instance3);
                }
                case 392: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_392(n7, n8, n9, memory2, instance3);
                }
                case 393: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_393(n7, n8, n9, memory2, instance3);
                }
                case 394: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_394(n7, n8, n9, memory2, instance3);
                }
                case 395: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_395(n7, n8, n9, memory2, instance3);
                }
                case 397: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_397(n7, n8, n9, memory2, instance3);
                }
                case 400: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_400(n7, n8, n9, memory2, instance3);
                }
                case 401: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_401(n7, n8, n9, memory2, instance3);
                }
                case 402: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_402(n7, n8, n9, memory2, instance3);
                }
                case 406: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_406(n7, n8, n9, memory2, instance3);
                }
                case 407: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_407(n7, n8, n9, memory2, instance3);
                }
                case 408: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_408(n7, n8, n9, memory2, instance3);
                }
                case 409: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_409(n7, n8, n9, memory2, instance3);
                }
                case 411: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_411(n7, n8, n9, memory2, instance3);
                }
                case 412: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_412(n7, n8, n9, memory2, instance3);
                }
                case 414: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_414(n7, n8, n9, memory2, instance3);
                }
                case 416: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_416(n7, n8, n9, memory2, instance3);
                }
                case 419: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_419(n7, n8, n9, memory2, instance3);
                }
                case 420: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_420(n7, n8, n9, memory2, instance3);
                }
                case 421: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_421(n7, n8, n9, memory2, instance3);
                }
                case 431: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_431(n7, n8, n9, memory2, instance3);
                }
                case 432: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_432(n7, n8, n9, memory2, instance3);
                }
                case 433: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_433(n7, n8, n9, memory2, instance3);
                }
                case 435: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_435(n7, n8, n9, memory2, instance3);
                }
                case 436: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_436(n7, n8, n9, memory2, instance3);
                }
                case 437: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_437(n7, n8, n9, memory2, instance3);
                }
                case 438: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_438(n7, n8, n9, memory2, instance3);
                }
                case 440: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_440(n7, n8, n9, memory2, instance3);
                }
                case 441: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_441(n7, n8, n9, memory2, instance3);
                }
                case 442: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_442(n7, n8, n9, memory2, instance3);
                }
                case 443: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_443(n7, n8, n9, memory2, instance3);
                }
                case 444: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_444(n7, n8, n9, memory2, instance3);
                }
                case 457: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_457(n7, n8, n9, memory2, instance3);
                }
                case 514: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_514(n7, n8, n9, memory2, instance3);
                }
                case 539: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_539(n7, n8, n9, memory2, instance3);
                }
                case 547: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_547(n7, n8, n9, memory2, instance3);
                }
                case 553: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_553(n7, n8, n9, memory2, instance3);
                }
                case 555: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_555(n7, n8, n9, memory2, instance3);
                }
                case 572: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_572(n7, n8, n9, memory2, instance3);
                }
                case 582: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_582(n7, n8, n9, memory2, instance3);
                }
                case 593: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_593(n7, n8, n9, memory2, instance3);
                }
                case 606: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_606(n7, n8, n9, memory2, instance3);
                }
                case 608: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_608(n7, n8, n9, memory2, instance3);
                }
                case 610: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_610(n7, n8, n9, memory2, instance3);
                }
                case 612: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_612(n7, n8, n9, memory2, instance3);
                }
                case 617: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_617(n7, n8, n9, memory2, instance3);
                }
                case 624: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_624(n7, n8, n9, memory2, instance3);
                }
                case 626: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_626(n7, n8, n9, memory2, instance3);
                }
                case 628: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_628(n7, n8, n9, memory2, instance3);
                }
                case 636: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_636(n7, n8, n9, memory2, instance3);
                }
                case 638: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_638(n7, n8, n9, memory2, instance3);
                }
                case 639: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_639(n7, n8, n9, memory2, instance3);
                }
                case 652: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_652(n7, n8, n9, memory2, instance3);
                }
                case 686: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_686(n7, n8, n9, memory2, instance3);
                }
                case 702: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_702(n7, n8, n9, memory2, instance3);
                }
                case 706: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_706(n7, n8, n9, memory2, instance3);
                }
                case 708: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_708(n7, n8, n9, memory2, instance3);
                }
                case 710: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_710(n7, n8, n9, memory2, instance3);
                }
                case 722: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_722(n7, n8, n9, memory2, instance3);
                }
                case 739: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_739(n7, n8, n9, memory2, instance3);
                }
                case 740: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_740(n7, n8, n9, memory2, instance3);
                }
                case 741: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_741(n7, n8, n9, memory2, instance3);
                }
                case 757: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_757(n7, n8, n9, memory2, instance3);
                }
                case 780: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_780(n7, n8, n9, memory2, instance3);
                }
                case 783: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_783(n7, n8, n9, memory2, instance3);
                }
                case 788: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_788(n7, n8, n9, memory2, instance3);
                }
                case 790: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_790(n7, n8, n9, memory2, instance3);
                }
                case 803: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_803(n7, n8, n9, memory2, instance3);
                }
                case 804: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_804(n7, n8, n9, memory2, instance3);
                }
                case 805: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_805(n7, n8, n9, memory2, instance3);
                }
                case 806: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_806(n7, n8, n9, memory2, instance3);
                }
                case 807: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_807(n7, n8, n9, memory2, instance3);
                }
                case 808: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_808(n7, n8, n9, memory2, instance3);
                }
                case 811: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_811(n7, n8, n9, memory2, instance3);
                }
                case 812: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_812(n7, n8, n9, memory2, instance3);
                }
                case 813: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_813(n7, n8, n9, memory2, instance3);
                }
                case 814: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_814(n7, n8, n9, memory2, instance3);
                }
                case 815: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_815(n7, n8, n9, memory2, instance3);
                }
                case 817: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_817(n7, n8, n9, memory2, instance3);
                }
                case 818: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_818(n7, n8, n9, memory2, instance3);
                }
                case 826: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_826(n7, n8, n9, memory2, instance3);
                }
                case 828: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_828(n7, n8, n9, memory2, instance3);
                }
                case 856: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_856(n7, n8, n9, memory2, instance3);
                }
                case 870: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_870(n7, n8, n9, memory2, instance3);
                }
                case 897: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_897(n7, n8, n9, memory2, instance3);
                }
                case 906: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_906(n7, n8, n9, memory2, instance3);
                }
                case 907: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_907(n7, n8, n9, memory2, instance3);
                }
                case 931: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_931(n7, n8, n9, memory2, instance3);
                }
                case 955: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_955(n7, n8, n9, memory2, instance3);
                }
                case 959: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_959(n7, n8, n9, memory2, instance3);
                }
                case 961: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_961(n7, n8, n9, memory2, instance3);
                }
                case 963: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_963(n7, n8, n9, memory2, instance3);
                }
                case 976: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_976(n7, n8, n9, memory2, instance3);
                }
                case 977: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_977(n7, n8, n9, memory2, instance3);
                }
                case 983: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_983(n7, n8, n9, memory2, instance3);
                }
                case 991: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_991(n7, n8, n9, memory2, instance3);
                }
                case 1001: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1001(n7, n8, n9, memory2, instance3);
                }
                case 1002: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1002(n7, n8, n9, memory2, instance3);
                }
                case 1004: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1004(n7, n8, n9, memory2, instance3);
                }
                case 1006: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1006(n7, n8, n9, memory2, instance3);
                }
                case 1026: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1026(n7, n8, n9, memory2, instance3);
                }
                case 1030: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1030(n7, n8, n9, memory2, instance3);
                }
                case 1032: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1032(n7, n8, n9, memory2, instance3);
                }
                case 1036: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1036(n7, n8, n9, memory2, instance3);
                }
                case 1037: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1037(n7, n8, n9, memory2, instance3);
                }
                case 1040: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1040(n7, n8, n9, memory2, instance3);
                }
                case 1048: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1048(n7, n8, n9, memory2, instance3);
                }
                case 1054: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1054(n7, n8, n9, memory2, instance3);
                }
                case 1071: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1071(n7, n8, n9, memory2, instance3);
                }
                case 1074: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1074(n7, n8, n9, memory2, instance3);
                }
                case 1078: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1078(n7, n8, n9, memory2, instance3);
                }
                case 1080: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1080(n7, n8, n9, memory2, instance3);
                }
                case 1082: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1082(n7, n8, n9, memory2, instance3);
                }
                case 1144: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1144(n7, n8, n9, memory2, instance3);
                }
                case 1163: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1163(n7, n8, n9, memory2, instance3);
                }
                case 1164: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1164(n7, n8, n9, memory2, instance3);
                }
                case 1170: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1170(n7, n8, n9, memory2, instance3);
                }
                case 1192: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1192(n7, n8, n9, memory2, instance3);
                }
                case 1196: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1196(n7, n8, n9, memory2, instance3);
                }
                case 1199: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1199(n7, n8, n9, memory2, instance3);
                }
                case 1202: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1202(n7, n8, n9, memory2, instance3);
                }
                case 1205: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1205(n7, n8, n9, memory2, instance3);
                }
                case 1230: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1230(n7, n8, n9, memory2, instance3);
                }
                case 1236: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1236(n7, n8, n9, memory2, instance3);
                }
                case 1259: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1259(n7, n8, n9, memory2, instance3);
                }
                case 1260: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1260(n7, n8, n9, memory2, instance3);
                }
                case 1261: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1261(n7, n8, n9, memory2, instance3);
                }
                case 1263: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1263(n7, n8, n9, memory2, instance3);
                }
                case 1265: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1265(n7, n8, n9, memory2, instance3);
                }
                case 1267: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1267(n7, n8, n9, memory2, instance3);
                }
                case 1268: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1268(n7, n8, n9, memory2, instance3);
                }
                case 1269: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1269(n7, n8, n9, memory2, instance3);
                }
                case 1270: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1270(n7, n8, n9, memory2, instance3);
                }
                case 1271: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1271(n7, n8, n9, memory2, instance3);
                }
                case 1274: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1274(n7, n8, n9, memory2, instance3);
                }
                case 1275: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1275(n7, n8, n9, memory2, instance3);
                }
                case 1276: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1276(n7, n8, n9, memory2, instance3);
                }
                case 1277: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1277(n7, n8, n9, memory2, instance3);
                }
                case 1278: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1278(n7, n8, n9, memory2, instance3);
                }
                case 1279: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1279(n7, n8, n9, memory2, instance3);
                }
                case 1280: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1280(n7, n8, n9, memory2, instance3);
                }
                case 1282: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1282(n7, n8, n9, memory2, instance3);
                }
                case 1285: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1285(n7, n8, n9, memory2, instance3);
                }
                case 1286: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1286(n7, n8, n9, memory2, instance3);
                }
                case 1287: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1287(n7, n8, n9, memory2, instance3);
                }
                case 1294: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1294(n7, n8, n9, memory2, instance3);
                }
                case 1300: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1300(n7, n8, n9, memory2, instance3);
                }
                case 1302: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1302(n7, n8, n9, memory2, instance3);
                }
                case 1372: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1372(n7, n8, n9, memory2, instance3);
                }
                case 1408: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1408(n7, n8, n9, memory2, instance3);
                }
                case 1412: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1412(n7, n8, n9, memory2, instance3);
                }
                case 1573: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1573(n7, n8, n9, memory2, instance3);
                }
                case 1575: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1575(n7, n8, n9, memory2, instance3);
                }
                case 1582: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1582(n7, n8, n9, memory2, instance3);
                }
                case 1585: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1585(n7, n8, n9, memory2, instance3);
                }
                case 1596: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1596(n7, n8, n9, memory2, instance3);
                }
                case 1597: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1597(n7, n8, n9, memory2, instance3);
                }
                case 1607: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1607(n7, n8, n9, memory2, instance3);
                }
                case 1619: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1619(n7, n8, n9, memory2, instance3);
                }
                case 1638: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1638(n7, n8, n9, memory2, instance3);
                }
                case 1644: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1644(n7, n8, n9, memory2, instance3);
                }
                case 1645: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1645(n7, n8, n9, memory2, instance3);
                }
                case 1649: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1649(n7, n8, n9, memory2, instance3);
                }
                case 1652: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1652(n7, n8, n9, memory2, instance3);
                }
                case 1654: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1654(n7, n8, n9, memory2, instance3);
                }
                case 1658: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1658(n7, n8, n9, memory2, instance3);
                }
                case 1659: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1659(n7, n8, n9, memory2, instance3);
                }
                case 1661: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1661(n7, n8, n9, memory2, instance3);
                }
                case 1663: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1663(n7, n8, n9, memory2, instance3);
                }
                case 1667: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1667(n7, n8, n9, memory2, instance3);
                }
                case 1671: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1671(n7, n8, n9, memory2, instance3);
                }
                case 1685: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1685(n7, n8, n9, memory2, instance3);
                }
                case 1698: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1698(n7, n8, n9, memory2, instance3);
                }
                case 1702: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1702(n7, n8, n9, memory2, instance3);
                }
                case 1705: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1705(n7, n8, n9, memory2, instance3);
                }
                case 1714: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1714(n7, n8, n9, memory2, instance3);
                }
                case 1737: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1737(n7, n8, n9, memory2, instance3);
                }
                case 1752: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1752(n7, n8, n9, memory2, instance3);
                }
                case 1753: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1753(n7, n8, n9, memory2, instance3);
                }
                case 1756: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1756(n7, n8, n9, memory2, instance3);
                }
                case 1800: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1800(n7, n8, n9, memory2, instance3);
                }
                case 1809: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1809(n7, n8, n9, memory2, instance3);
                }
                case 1816: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1816(n7, n8, n9, memory2, instance3);
                }
                case 1825: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1825(n7, n8, n9, memory2, instance3);
                }
                case 1836: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1836(n7, n8, n9, memory2, instance3);
                }
                case 1839: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1839(n7, n8, n9, memory2, instance3);
                }
                case 1840: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1840(n7, n8, n9, memory2, instance3);
                }
                case 1841: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1841(n7, n8, n9, memory2, instance3);
                }
                case 1842: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1842(n7, n8, n9, memory2, instance3);
                }
                case 1843: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1843(n7, n8, n9, memory2, instance3);
                }
                case 1847: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1847(n7, n8, n9, memory2, instance3);
                }
                case 1852: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1852(n7, n8, n9, memory2, instance3);
                }
                case 1855: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1855(n7, n8, n9, memory2, instance3);
                }
                case 1866: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1866(n7, n8, n9, memory2, instance3);
                }
                case 1867: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1867(n7, n8, n9, memory2, instance3);
                }
                case 1870: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1870(n7, n8, n9, memory2, instance3);
                }
                case 1876: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1876(n7, n8, n9, memory2, instance3);
                }
                case 1888: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1888(n7, n8, n9, memory2, instance3);
                }
                case 1889: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1889(n7, n8, n9, memory2, instance3);
                }
                case 1890: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1890(n7, n8, n9, memory2, instance3);
                }
                case 1891: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1891(n7, n8, n9, memory2, instance3);
                }
                case 1892: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1892(n7, n8, n9, memory2, instance3);
                }
                case 1898: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1898(n7, n8, n9, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3}, 6, n6, instance2)[0];
    }

    public static int call_indirect_7(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n8);
        int n9 = tableInstance.requiredRef(n7);
        Instance instance2 = tableInstance.instance(n7);
        if (instance2 == null || instance2 == instance) {
            int n10 = n;
            int n11 = n2;
            int n12 = n3;
            int n13 = n4;
            int n14 = n5;
            int n15 = n6;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n9) {
                case 366: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_366(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
                case 580: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_580(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
                case 584: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_584(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
                case 588: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_588(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
                case 838: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_838(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
                case 852: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_852(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
                case 861: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_861(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
                case 866: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_866(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
                case 1237: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1237(n10, n11, n12, n13, n14, n15, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5, n6}, 7, n9, instance2)[0];
    }

    public static int call_indirect_8(int n, int n2, int n3, int n4, int n5, int n6, int n7, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n7);
        int n8 = tableInstance.requiredRef(n6);
        Instance instance2 = tableInstance.instance(n6);
        if (instance2 == null || instance2 == instance) {
            int n9 = n;
            int n10 = n2;
            int n11 = n3;
            int n12 = n4;
            int n13 = n5;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n8) {
                case 12: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_12(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 17: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_17(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 81: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_81(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 245: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_245(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 259: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_259(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 260: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_260(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 349: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_349(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 352: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_352(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 356: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_356(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 358: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_358(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 583: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_583(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 592: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_592(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 622: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_622(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 627: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_627(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 630: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_630(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 631: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_631(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 633: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_633(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 634: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_634(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 792: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_792(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 860: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_860(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 890: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_890(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 909: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_909(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1041: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1041(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1044: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1044(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1063: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1063(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1064: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1064(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1066: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1066(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1067: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1067(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1308: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1308(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1577: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1577(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1653: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1653(n9, n10, n11, n12, n13, memory2, instance3);
                }
                case 1871: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1871(n9, n10, n11, n12, n13, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5}, 8, n8, instance2)[0];
    }

    public static int call_indirect_9(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n9);
        int n10 = tableInstance.requiredRef(n8);
        Instance instance2 = tableInstance.instance(n8);
        if (instance2 == null || instance2 == instance) {
            int n11 = n;
            int n12 = n2;
            int n13 = n3;
            int n14 = n4;
            int n15 = n5;
            int n16 = n6;
            int n17 = n7;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n10) {
                case 338: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_338(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
                case 585: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_585(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
                case 586: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_586(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
                case 587: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_587(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
                case 629: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_629(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
                case 863: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_863(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
                case 864: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_864(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
                case 865: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_865(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
                case 1062: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1062(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5, n6, n7}, 9, n10, instance2)[0];
    }

    public static int call_indirect_10(int n, long l, int n2, int n3, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n3);
        int n4 = tableInstance.requiredRef(n2);
        Instance instance2 = tableInstance.instance(n2);
        if (instance2 == null || instance2 == instance) {
            int n5 = n;
            long l2 = l;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n4) {
                case 600: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_600(n5, l2, memory2, instance3);
                }
                case 603: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_603(n5, l2, memory2, instance3);
                }
                case 793: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_793(n5, l2, memory2, instance3);
                }
                case 795: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_795(n5, l2, memory2, instance3);
                }
                case 910: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_910(n5, l2, memory2, instance3);
                }
                case 912: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_912(n5, l2, memory2, instance3);
                }
                case 943: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_943(n5, l2, memory2, instance3);
                }
                case 948: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_948(n5, l2, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, l}, 10, n4, instance2)[0];
    }

    public static int call_indirect_11(int n, int n2, long l, int n3, int n4, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n4);
        int n5 = tableInstance.requiredRef(n3);
        Instance instance2 = tableInstance.instance(n3);
        if (instance2 == null || instance2 == instance) {
            int n6 = n;
            int n7 = n2;
            long l2 = l;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n5) {
                case 327: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_327(n6, n7, l2, memory2, instance3);
                }
                case 609: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_609(n6, n7, l2, memory2, instance3);
                }
                case 990: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_990(n6, n7, l2, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, l}, 11, n5, instance2)[0];
    }

    public static int call_indirect_12(int n, int n2, int n3, long l, int n4, int n5, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n5);
        int n6 = tableInstance.requiredRef(n4);
        Instance instance2 = tableInstance.instance(n4);
        if (instance2 == null || instance2 == instance) {
            int n7 = n;
            int n8 = n2;
            int n9 = n3;
            long l2 = l;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n6) {
                case 328: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_328(n7, n8, n9, l2, memory2, instance3);
                }
                case 329: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_329(n7, n8, n9, l2, memory2, instance3);
                }
                case 425: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_425(n7, n8, n9, l2, memory2, instance3);
                }
                case 613: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_613(n7, n8, n9, l2, memory2, instance3);
                }
                case 640: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_640(n7, n8, n9, l2, memory2, instance3);
                }
                case 1014: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1014(n7, n8, n9, l2, memory2, instance3);
                }
                case 1083: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1083(n7, n8, n9, l2, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, l}, 12, n6, instance2)[0];
    }

    public static int call_indirect_13(int n, int n2, int n3, long l, int n4, int n5, int n6, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n6);
        int n7 = tableInstance.requiredRef(n5);
        Instance instance2 = tableInstance.instance(n5);
        if (instance2 == null || instance2 == instance) {
            int n8 = n;
            int n9 = n2;
            int n10 = n3;
            long l2 = l;
            int n11 = n4;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n7) {
                case 635: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_635(n8, n9, n10, l2, n11, memory2, instance3);
                }
                case 1069: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1069(n8, n9, n10, l2, n11, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, l, n4}, 13, n7, instance2)[0];
    }

    public static int call_indirect_14(int n, int n2, int n3, long l, long l2, int n4, int n5, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n5);
        int n6 = tableInstance.requiredRef(n4);
        Instance instance2 = tableInstance.instance(n4);
        if (instance2 == null || instance2 == instance) {
            int n7 = n;
            int n8 = n2;
            int n9 = n3;
            long l3 = l;
            long l4 = l2;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n6) {
                case 677: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_677(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 678: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_678(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 679: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_679(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 680: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_680(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 681: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_681(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 683: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_683(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 698: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_698(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 724: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_724(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 725: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_725(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 726: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_726(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 916: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_916(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 917: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_917(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 918: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_918(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 919: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_919(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 920: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_920(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 922: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_922(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 950: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_950(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 979: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_979(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 994: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_994(n7, n8, n9, l3, l4, memory2, instance3);
                }
                case 995: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_995(n7, n8, n9, l3, l4, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, l, l2}, 14, n6, instance2)[0];
    }

    public static int call_indirect_15(int n, int n2, int n3, long l, long l2, long l3, int n4, int n5, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n5);
        int n6 = tableInstance.requiredRef(n4);
        Instance instance2 = tableInstance.instance(n4);
        if (instance2 == null || instance2 == instance) {
            int n7 = n;
            int n8 = n2;
            int n9 = n3;
            long l4 = l;
            long l5 = l2;
            long l6 = l3;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n6) {
                case 732: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_732(n7, n8, n9, l4, l5, l6, memory2, instance3);
                }
                case 733: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_733(n7, n8, n9, l4, l5, l6, memory2, instance3);
                }
                case 992: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_992(n7, n8, n9, l4, l5, l6, memory2, instance3);
                }
                case 993: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_993(n7, n8, n9, l4, l5, l6, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, l, l2, l3}, 15, n6, instance2)[0];
    }

    public static void call_indirect_16(int n, int n2, int n3, int n4, int n5, int n6, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n6);
        int n7 = tableInstance.requiredRef(n5);
        Instance instance2 = tableInstance.instance(n5);
        if (instance2 == null || instance2 == instance) {
            int n8 = n;
            int n9 = n2;
            int n10 = n3;
            int n11 = n4;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n7) {
                case 54: {
                    Wat2WasmModuleMachineFuncGroup_0.func_54(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 63: {
                    Wat2WasmModuleMachineFuncGroup_0.func_63(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 73: {
                    Wat2WasmModuleMachineFuncGroup_0.func_73(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 114: {
                    Wat2WasmModuleMachineFuncGroup_0.func_114(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 137: {
                    Wat2WasmModuleMachineFuncGroup_0.func_137(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 146: {
                    Wat2WasmModuleMachineFuncGroup_0.func_146(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 201: {
                    Wat2WasmModuleMachineFuncGroup_0.func_201(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 206: {
                    Wat2WasmModuleMachineFuncGroup_0.func_206(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 207: {
                    Wat2WasmModuleMachineFuncGroup_0.func_207(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 225: {
                    Wat2WasmModuleMachineFuncGroup_0.func_225(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 451: {
                    Wat2WasmModuleMachineFuncGroup_0.func_451(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 452: {
                    Wat2WasmModuleMachineFuncGroup_0.func_452(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 459: {
                    Wat2WasmModuleMachineFuncGroup_0.func_459(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 564: {
                    Wat2WasmModuleMachineFuncGroup_0.func_564(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 566: {
                    Wat2WasmModuleMachineFuncGroup_0.func_566(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 569: {
                    Wat2WasmModuleMachineFuncGroup_0.func_569(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1086: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1086(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1100: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1100(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1151: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1151(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1193: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1193(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1208: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1208(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1309: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1309(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1666: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1666(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1670: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1670(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1686: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1686(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1687: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1687(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1688: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1688(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1696: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1696(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1718: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1718(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1722: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1722(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1758: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1758(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1759: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1759(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1760: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1760(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1761: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1761(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1763: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1763(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
                case 1765: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1765(n8, n9, n10, n11, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4}, 16, n7, instance2);
    }

    public static void call_indirect_17(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n8);
        int n9 = tableInstance.requiredRef(n7);
        Instance instance2 = tableInstance.instance(n7);
        if (instance2 == null || instance2 == instance) {
            int n10 = n;
            int n11 = n2;
            int n12 = n3;
            int n13 = n4;
            int n14 = n5;
            int n15 = n6;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n9) {
                case 210: {
                    Wat2WasmModuleMachineFuncGroup_0.func_210(n10, n11, n12, n13, n14, n15, memory2, instance3);
                    return;
                }
                case 221: {
                    Wat2WasmModuleMachineFuncGroup_0.func_221(n10, n11, n12, n13, n14, n15, memory2, instance3);
                    return;
                }
                case 1767: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1767(n10, n11, n12, n13, n14, n15, memory2, instance3);
                    return;
                }
                case 1771: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1771(n10, n11, n12, n13, n14, n15, memory2, instance3);
                    return;
                }
                case 1772: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1772(n10, n11, n12, n13, n14, n15, memory2, instance3);
                    return;
                }
                case 1773: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1773(n10, n11, n12, n13, n14, n15, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5, n6}, 17, n9, instance2);
    }

    public static void call_indirect_18(int n, int n2, int n3, int n4, int n5, int n6, int n7, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n7);
        int n8 = tableInstance.requiredRef(n6);
        Instance instance2 = tableInstance.instance(n6);
        if (instance2 == null || instance2 == instance) {
            int n9 = n;
            int n10 = n2;
            int n11 = n3;
            int n12 = n4;
            int n13 = n5;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n8) {
                case 118: {
                    Wat2WasmModuleMachineFuncGroup_0.func_118(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 208: {
                    Wat2WasmModuleMachineFuncGroup_0.func_208(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 224: {
                    Wat2WasmModuleMachineFuncGroup_0.func_224(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 249: {
                    Wat2WasmModuleMachineFuncGroup_0.func_249(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 253: {
                    Wat2WasmModuleMachineFuncGroup_0.func_253(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 1310: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1310(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 1691: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1691(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 1764: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1764(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 1766: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1766(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 1768: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1768(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 1769: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1769(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 1770: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1770(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
                case 1873: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1873(n9, n10, n11, n12, n13, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5}, 18, n8, instance2);
    }

    public static void call_indirect_19(int n, int n2, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n2);
        int n3 = tableInstance.requiredRef(n);
        Instance instance2 = tableInstance.instance(n);
        if (instance2 == null || instance2 == instance) {
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n3) {
                case 15: {
                    Wat2WasmModuleMachineFuncGroup_0.func_15(memory2, instance3);
                    return;
                }
                case 16: {
                    Wat2WasmModuleMachineFuncGroup_0.func_16(memory2, instance3);
                    return;
                }
                case 18: {
                    Wat2WasmModuleMachineFuncGroup_0.func_18(memory2, instance3);
                    return;
                }
                case 35: {
                    Wat2WasmModuleMachineFuncGroup_0.func_35(memory2, instance3);
                    return;
                }
                case 45: {
                    Wat2WasmModuleMachineFuncGroup_0.func_45(memory2, instance3);
                    return;
                }
                case 46: {
                    Wat2WasmModuleMachineFuncGroup_0.func_46(memory2, instance3);
                    return;
                }
                case 74: {
                    Wat2WasmModuleMachineFuncGroup_0.func_74(memory2, instance3);
                    return;
                }
                case 75: {
                    Wat2WasmModuleMachineFuncGroup_0.func_75(memory2, instance3);
                    return;
                }
                case 76: {
                    Wat2WasmModuleMachineFuncGroup_0.func_76(memory2, instance3);
                    return;
                }
                case 85: {
                    Wat2WasmModuleMachineFuncGroup_0.func_85(memory2, instance3);
                    return;
                }
                case 215: {
                    Wat2WasmModuleMachineFuncGroup_0.func_215(memory2, instance3);
                    return;
                }
                case 1152: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1152(memory2, instance3);
                    return;
                }
                case 1807: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1807(memory2, instance3);
                    return;
                }
                case 1817: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1817(memory2, instance3);
                    return;
                }
                case 1820: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1820(memory2, instance3);
                    return;
                }
                case 1821: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1821(memory2, instance3);
                    return;
                }
                case 1824: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1824(memory2, instance3);
                    return;
                }
                case 1826: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1826(memory2, instance3);
                    return;
                }
                case 1849: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1849(memory2, instance3);
                    return;
                }
                case 1858: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1858(memory2, instance3);
                    return;
                }
                case 1874: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1874(memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[0], 19, n3, instance2);
    }

    public static long call_indirect_20(int n, long l, int n2, int n3, int n4, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n4);
        int n5 = tableInstance.requiredRef(n3);
        Instance instance2 = tableInstance.instance(n3);
        if (instance2 == null || instance2 == instance) {
            int n6 = n;
            long l2 = l;
            int n7 = n2;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n5) {
                case 1837: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1837(n6, l2, n7, memory2, instance3);
                }
                case 1838: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1838(n6, l2, n7, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, l, n2}, 20, n5, instance2)[0];
    }

    public static int call_indirect_21(int n, long l, int n2, int n3, int n4, int n5, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n5);
        int n6 = tableInstance.requiredRef(n4);
        Instance instance2 = tableInstance.instance(n4);
        if (instance2 == null || instance2 == instance) {
            int n7 = n;
            long l2 = l;
            int n8 = n2;
            int n9 = n3;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n6) {
                case 10: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_10(n7, l2, n8, n9, memory2, instance3);
                }
                case 1802: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1802(n7, l2, n8, n9, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, l, n2, n3}, 21, n6, instance2)[0];
    }

    public static int call_indirect_22(int n, int n2, int n3, int n4, int n5, long l, long l2, int n6, int n7, int n8, int n9, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n9);
        int n10 = tableInstance.requiredRef(n8);
        Instance instance2 = tableInstance.instance(n8);
        if (instance2 == null || instance2 == instance) {
            switch (n10) {
                case 13: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_13(n, n2, n3, n4, n5, l, l2, n6, n7, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5, l, l2, n6, n7}, 22, n10, instance2)[0];
    }

    public static void call_indirect_23(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n10);
        int n11 = tableInstance.requiredRef(n9);
        Instance instance2 = tableInstance.instance(n9);
        if (instance2 == null || instance2 == instance) {
            int n12 = n;
            int n13 = n2;
            int n14 = n3;
            int n15 = n4;
            int n16 = n5;
            int n17 = n6;
            int n18 = n7;
            int n19 = n8;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n11) {
                case 87: {
                    Wat2WasmModuleMachineFuncGroup_0.func_87(n12, n13, n14, n15, n16, n17, n18, n19, memory2, instance3);
                    return;
                }
                case 1598: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1598(n12, n13, n14, n15, n16, n17, n18, n19, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5, n6, n7, n8}, 23, n11, instance2);
    }

    public static void call_indirect_24(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n9);
        int n10 = tableInstance.requiredRef(n8);
        Instance instance2 = tableInstance.instance(n8);
        if (instance2 == null || instance2 == instance) {
            int n11 = n;
            int n12 = n2;
            int n13 = n3;
            int n14 = n4;
            int n15 = n5;
            int n16 = n6;
            int n17 = n7;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n10) {
                case 88: {
                    Wat2WasmModuleMachineFuncGroup_0.func_88(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                    return;
                }
                case 222: {
                    Wat2WasmModuleMachineFuncGroup_0.func_222(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                    return;
                }
                case 1643: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1643(n11, n12, n13, n14, n15, n16, n17, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5, n6, n7}, 24, n10, instance2);
    }

    public static void call_indirect_25(int n, long l, int n2, int n3, int n4, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n4);
        int n5 = tableInstance.requiredRef(n3);
        Instance instance2 = tableInstance.instance(n3);
        if (instance2 == null || instance2 == instance) {
            int n6 = n;
            long l2 = l;
            int n7 = n2;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n5) {
                case 174: {
                    Wat2WasmModuleMachineFuncGroup_0.func_174(n6, l2, n7, memory2, instance3);
                    return;
                }
                case 175: {
                    Wat2WasmModuleMachineFuncGroup_0.func_175(n6, l2, n7, memory2, instance3);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, l, n2}, 25, n5, instance2);
    }

    public static long call_indirect_26(int n, long l, int n2, int n3, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n3);
        int n4 = tableInstance.requiredRef(n2);
        Instance instance2 = tableInstance.instance(n2);
        if (instance2 == null || instance2 == instance) {
            switch (n4) {
                case 193: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_193(n, l, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, l}, 26, n4, instance2)[0];
    }

    public static int call_indirect_27(int n, int n2, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n2);
        int n3 = tableInstance.requiredRef(n);
        Instance instance2 = tableInstance.instance(n);
        if (instance2 == null || instance2 == instance) {
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n3) {
                case 240: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_240(memory2, instance3);
                }
                case 241: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_241(memory2, instance3);
                }
                case 1631: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1631(memory2, instance3);
                }
                case 1677: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1677(memory2, instance3);
                }
                case 1775: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1775(memory2, instance3);
                }
                case 1791: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1791(memory2, instance3);
                }
                case 1857: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1857(memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[0], 27, n3, instance2)[0];
    }

    public static int call_indirect_28(int n, int n2, int n3, int n4, long l, long l2, int n5, int n6, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n6);
        int n7 = tableInstance.requiredRef(n5);
        Instance instance2 = tableInstance.instance(n5);
        if (instance2 == null || instance2 == instance) {
            int n8 = n;
            int n9 = n2;
            int n10 = n3;
            int n11 = n4;
            long l3 = l;
            long l4 = l2;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n7) {
                case 374: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_374(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 375: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_375(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 376: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_376(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 377: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_377(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 378: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_378(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 379: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_379(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 403: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_403(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 404: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_404(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 405: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_405(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
                case 429: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_429(n8, n9, n10, n11, l3, l4, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, l, l2}, 28, n7, instance2)[0];
    }

    public static int call_indirect_29(int n, int n2, int n3, int n4, long l, long l2, long l3, int n5, int n6, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n6);
        int n7 = tableInstance.requiredRef(n5);
        Instance instance2 = tableInstance.instance(n5);
        if (instance2 == null || instance2 == instance) {
            int n8 = n;
            int n9 = n2;
            int n10 = n3;
            int n11 = n4;
            long l4 = l;
            long l5 = l2;
            long l6 = l3;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n7) {
                case 426: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_426(n8, n9, n10, n11, l4, l5, l6, memory2, instance3);
                }
                case 427: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_427(n8, n9, n10, n11, l4, l5, l6, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, l, l2, l3}, 29, n7, instance2)[0];
    }

    public static int call_indirect_30(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n10);
        int n11 = tableInstance.requiredRef(n9);
        Instance instance2 = tableInstance.instance(n9);
        if (instance2 == null || instance2 == instance) {
            switch (n11) {
                case 827: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_827(n, n2, n3, n4, n5, n6, n7, n8, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5, n6, n7, n8}, 30, n11, instance2)[0];
    }

    public static int call_indirect_31(int n, long l, int n2, long l2, int n3, int n4, int n5, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n5);
        int n6 = tableInstance.requiredRef(n4);
        Instance instance2 = tableInstance.instance(n4);
        if (instance2 == null || instance2 == instance) {
            switch (n6) {
                case 829: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_829(n, l, n2, l2, n3, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, l, n2, l2, n3}, 31, n6, instance2)[0];
    }

    public static int call_indirect_32(int n, int n2, int n3, int n4, long l, long l2, int n5, int n6, int n7, int n8, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n8);
        int n9 = tableInstance.requiredRef(n7);
        Instance instance2 = tableInstance.instance(n7);
        if (instance2 == null || instance2 == instance) {
            switch (n9) {
                case 1805: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1805(n, n2, n3, n4, l, l2, n5, n6, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, l, l2, n5, n6}, 32, n9, instance2)[0];
    }

    public static double call_indirect_33(double d, int n, int n2, int n3, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n3);
        int n4 = tableInstance.requiredRef(n2);
        Instance instance2 = tableInstance.instance(n2);
        if (instance2 == null || instance2 == instance) {
            double d2 = d;
            int n5 = n;
            Memory memory2 = memory;
            Instance instance3 = instance;
            switch (n4) {
                case 1869: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1869(d2, n5, memory2, instance3);
                }
                case 1881: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1881(d2, n5, memory2, instance3);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[]{Value.doubleToLong(d), n}, 33, n4, instance2)[0]);
    }

    public static void call_indirect_34(int n, long l, int n2, int n3, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n3);
        int n4 = tableInstance.requiredRef(n2);
        Instance instance2 = tableInstance.instance(n2);
        if (instance2 == null || instance2 == instance) {
            switch (n4) {
                case 1879: {
                    Wat2WasmModuleMachineFuncGroup_0.func_1879(n, l, memory, instance);
                    return;
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        long[] lArray = Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, l}, 34, n4, instance2);
    }

    public static double call_indirect_35(double d, double d2, int n, int n2, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n2);
        int n3 = tableInstance.requiredRef(n);
        Instance instance2 = tableInstance.instance(n);
        if (instance2 == null || instance2 == instance) {
            switch (n3) {
                case 1882: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1882(d, d2, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[]{Value.doubleToLong(d), Value.doubleToLong(d2)}, 35, n3, instance2)[0]);
    }

    public static double call_indirect_36(int n, int n2, int n3, int n4, int n5, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n5);
        int n6 = tableInstance.requiredRef(n4);
        Instance instance2 = tableInstance.instance(n4);
        if (instance2 == null || instance2 == instance) {
            switch (n6) {
                case 1883: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1883(n, n2, n3, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3}, 36, n6, instance2)[0]);
    }

    public static double call_indirect_37(int n, int n2, int n3, int n4, int n5, int n6, int n7, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n7);
        int n8 = tableInstance.requiredRef(n6);
        Instance instance2 = tableInstance.instance(n6);
        if (instance2 == null || instance2 == instance) {
            switch (n8) {
                case 1884: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1884(n, n2, n3, n4, n5, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2, n3, n4, n5}, 37, n8, instance2)[0]);
    }

    public static long call_indirect_38(int n, int n2, int n3, int n4, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n4);
        int n5 = tableInstance.requiredRef(n3);
        Instance instance2 = tableInstance.instance(n3);
        if (instance2 == null || instance2 == instance) {
            switch (n5) {
                case 1885: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1885(n, n2, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2}, 38, n5, instance2)[0];
    }

    public static float call_indirect_39(int n, int n2, int n3, int n4, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n4);
        int n5 = tableInstance.requiredRef(n3);
        Instance instance2 = tableInstance.instance(n3);
        if (instance2 == null || instance2 == instance) {
            switch (n5) {
                case 1886: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1886(n, n2, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Value.longToFloat(Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2}, 39, n5, instance2)[0]);
    }

    public static double call_indirect_40(int n, int n2, int n3, int n4, Memory memory, Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        TableInstance tableInstance = instance.table(n4);
        int n5 = tableInstance.requiredRef(n3);
        Instance instance2 = tableInstance.instance(n3);
        if (instance2 == null || instance2 == instance) {
            switch (n5) {
                case 1887: {
                    return Wat2WasmModuleMachineFuncGroup_0.func_1887(n, n2, memory, instance);
                }
            }
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[]{n, n2}, 40, n5, instance2)[0]);
    }
}

