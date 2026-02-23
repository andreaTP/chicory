// 
// Decompiled by Procyon v0.6.0
// 

package com.dylibso.chicory.wabt;

import com.dylibso.chicory.wasm.types.Value;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Machine;

public final class Wat2WasmModuleMachine implements Machine
{
    private final Instance instance;
    
    public Wat2WasmModuleMachine(final Instance instance) {
        this.instance = instance;
    }
    
    public long[] call(final int n, final long[] array) {
        try {
            final Instance instance = this.instance;
            return Wat2WasmModuleMachineMachineCall.call(instance, instance.memory(), n, array);
        }
        catch (final StackOverflowError e) {
            throw Wat2WasmModuleMachineShaded.throwCallStackExhausted(e);
        }
    }
    
    public static void call_indirect_0(final int n, final int n2, final int n3, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n3);
        final int requiredRef = table.requiredRef(n2);
        final Instance instance2 = table.instance(n2);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n }, 0, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 14: {
                Wat2WasmModuleMachineFuncGroup_0.func_14(n, memory, instance);
                return;
            }
            case 19: {
                Wat2WasmModuleMachineFuncGroup_0.func_19(n, memory, instance);
                return;
            }
            case 20: {
                Wat2WasmModuleMachineFuncGroup_0.func_20(n, memory, instance);
                return;
            }
            case 22: {
                Wat2WasmModuleMachineFuncGroup_0.func_22(n, memory, instance);
                return;
            }
            case 23: {
                Wat2WasmModuleMachineFuncGroup_0.func_23(n, memory, instance);
                return;
            }
            case 24: {
                Wat2WasmModuleMachineFuncGroup_0.func_24(n, memory, instance);
                return;
            }
            case 26: {
                Wat2WasmModuleMachineFuncGroup_0.func_26(n, memory, instance);
                return;
            }
            case 27: {
                Wat2WasmModuleMachineFuncGroup_0.func_27(n, memory, instance);
                return;
            }
            case 28: {
                Wat2WasmModuleMachineFuncGroup_0.func_28(n, memory, instance);
                return;
            }
            case 29: {
                Wat2WasmModuleMachineFuncGroup_0.func_29(n, memory, instance);
                return;
            }
            case 31: {
                Wat2WasmModuleMachineFuncGroup_0.func_31(n, memory, instance);
                return;
            }
            case 90: {
                Wat2WasmModuleMachineFuncGroup_0.func_90(n, memory, instance);
                return;
            }
            case 91: {
                Wat2WasmModuleMachineFuncGroup_0.func_91(n, memory, instance);
                return;
            }
            case 92: {
                Wat2WasmModuleMachineFuncGroup_0.func_92(n, memory, instance);
                return;
            }
            case 93: {
                Wat2WasmModuleMachineFuncGroup_0.func_93(n, memory, instance);
                return;
            }
            case 94: {
                Wat2WasmModuleMachineFuncGroup_0.func_94(n, memory, instance);
                return;
            }
            case 95: {
                Wat2WasmModuleMachineFuncGroup_0.func_95(n, memory, instance);
                return;
            }
            case 96: {
                Wat2WasmModuleMachineFuncGroup_0.func_96(n, memory, instance);
                return;
            }
            case 97: {
                Wat2WasmModuleMachineFuncGroup_0.func_97(n, memory, instance);
                return;
            }
            case 98: {
                Wat2WasmModuleMachineFuncGroup_0.func_98(n, memory, instance);
                return;
            }
            case 99: {
                Wat2WasmModuleMachineFuncGroup_0.func_99(n, memory, instance);
                return;
            }
            case 100: {
                Wat2WasmModuleMachineFuncGroup_0.func_100(n, memory, instance);
                return;
            }
            case 101: {
                Wat2WasmModuleMachineFuncGroup_0.func_101(n, memory, instance);
                return;
            }
            case 102: {
                Wat2WasmModuleMachineFuncGroup_0.func_102(n, memory, instance);
                return;
            }
            case 103: {
                Wat2WasmModuleMachineFuncGroup_0.func_103(n, memory, instance);
                return;
            }
            case 104: {
                Wat2WasmModuleMachineFuncGroup_0.func_104(n, memory, instance);
                return;
            }
            case 105: {
                Wat2WasmModuleMachineFuncGroup_0.func_105(n, memory, instance);
                return;
            }
            case 106: {
                Wat2WasmModuleMachineFuncGroup_0.func_106(n, memory, instance);
                return;
            }
            case 107: {
                Wat2WasmModuleMachineFuncGroup_0.func_107(n, memory, instance);
                return;
            }
            case 108: {
                Wat2WasmModuleMachineFuncGroup_0.func_108(n, memory, instance);
                return;
            }
            case 109: {
                Wat2WasmModuleMachineFuncGroup_0.func_109(n, memory, instance);
                return;
            }
            case 200: {
                Wat2WasmModuleMachineFuncGroup_0.func_200(n, memory, instance);
                return;
            }
            case 202: {
                Wat2WasmModuleMachineFuncGroup_0.func_202(n, memory, instance);
                return;
            }
            case 213: {
                Wat2WasmModuleMachineFuncGroup_0.func_213(n, memory, instance);
                return;
            }
            case 217: {
                Wat2WasmModuleMachineFuncGroup_0.func_217(n, memory, instance);
                return;
            }
            case 219: {
                Wat2WasmModuleMachineFuncGroup_0.func_219(n, memory, instance);
                return;
            }
            case 235: {
                Wat2WasmModuleMachineFuncGroup_0.func_235(n, memory, instance);
                return;
            }
            case 236: {
                Wat2WasmModuleMachineFuncGroup_0.func_236(n, memory, instance);
                return;
            }
            case 243: {
                Wat2WasmModuleMachineFuncGroup_0.func_243(n, memory, instance);
                return;
            }
            case 244: {
                Wat2WasmModuleMachineFuncGroup_0.func_244(n, memory, instance);
                return;
            }
            case 460: {
                Wat2WasmModuleMachineFuncGroup_0.func_460(n, memory, instance);
                return;
            }
            case 799: {
                Wat2WasmModuleMachineFuncGroup_0.func_799(n, memory, instance);
                return;
            }
            case 831: {
                Wat2WasmModuleMachineFuncGroup_0.func_831(n, memory, instance);
                return;
            }
            case 832: {
                Wat2WasmModuleMachineFuncGroup_0.func_832(n, memory, instance);
                return;
            }
            case 841: {
                Wat2WasmModuleMachineFuncGroup_0.func_841(n, memory, instance);
                return;
            }
            case 1005: {
                Wat2WasmModuleMachineFuncGroup_0.func_1005(n, memory, instance);
                return;
            }
            case 1084: {
                Wat2WasmModuleMachineFuncGroup_0.func_1084(n, memory, instance);
                return;
            }
            case 1088: {
                Wat2WasmModuleMachineFuncGroup_0.func_1088(n, memory, instance);
                return;
            }
            case 1089: {
                Wat2WasmModuleMachineFuncGroup_0.func_1089(n, memory, instance);
                return;
            }
            case 1090: {
                Wat2WasmModuleMachineFuncGroup_0.func_1090(n, memory, instance);
                return;
            }
            case 1092: {
                Wat2WasmModuleMachineFuncGroup_0.func_1092(n, memory, instance);
                return;
            }
            case 1103: {
                Wat2WasmModuleMachineFuncGroup_0.func_1103(n, memory, instance);
                return;
            }
            case 1217: {
                Wat2WasmModuleMachineFuncGroup_0.func_1217(n, memory, instance);
                return;
            }
            case 1312: {
                Wat2WasmModuleMachineFuncGroup_0.func_1312(n, memory, instance);
                return;
            }
            case 1314: {
                Wat2WasmModuleMachineFuncGroup_0.func_1314(n, memory, instance);
                return;
            }
            case 1316: {
                Wat2WasmModuleMachineFuncGroup_0.func_1316(n, memory, instance);
                return;
            }
            case 1317: {
                Wat2WasmModuleMachineFuncGroup_0.func_1317(n, memory, instance);
                return;
            }
            case 1394: {
                Wat2WasmModuleMachineFuncGroup_0.func_1394(n, memory, instance);
                return;
            }
            case 1395: {
                Wat2WasmModuleMachineFuncGroup_0.func_1395(n, memory, instance);
                return;
            }
            case 1397: {
                Wat2WasmModuleMachineFuncGroup_0.func_1397(n, memory, instance);
                return;
            }
            case 1398: {
                Wat2WasmModuleMachineFuncGroup_0.func_1398(n, memory, instance);
                return;
            }
            case 1400: {
                Wat2WasmModuleMachineFuncGroup_0.func_1400(n, memory, instance);
                return;
            }
            case 1402: {
                Wat2WasmModuleMachineFuncGroup_0.func_1402(n, memory, instance);
                return;
            }
            case 1403: {
                Wat2WasmModuleMachineFuncGroup_0.func_1403(n, memory, instance);
                return;
            }
            case 1405: {
                Wat2WasmModuleMachineFuncGroup_0.func_1405(n, memory, instance);
                return;
            }
            case 1406: {
                Wat2WasmModuleMachineFuncGroup_0.func_1406(n, memory, instance);
                return;
            }
            case 1411: {
                Wat2WasmModuleMachineFuncGroup_0.func_1411(n, memory, instance);
                return;
            }
            case 1415: {
                Wat2WasmModuleMachineFuncGroup_0.func_1415(n, memory, instance);
                return;
            }
            case 1417: {
                Wat2WasmModuleMachineFuncGroup_0.func_1417(n, memory, instance);
                return;
            }
            case 1419: {
                Wat2WasmModuleMachineFuncGroup_0.func_1419(n, memory, instance);
                return;
            }
            case 1421: {
                Wat2WasmModuleMachineFuncGroup_0.func_1421(n, memory, instance);
                return;
            }
            case 1422: {
                Wat2WasmModuleMachineFuncGroup_0.func_1422(n, memory, instance);
                return;
            }
            case 1424: {
                Wat2WasmModuleMachineFuncGroup_0.func_1424(n, memory, instance);
                return;
            }
            case 1426: {
                Wat2WasmModuleMachineFuncGroup_0.func_1426(n, memory, instance);
                return;
            }
            case 1428: {
                Wat2WasmModuleMachineFuncGroup_0.func_1428(n, memory, instance);
                return;
            }
            case 1431: {
                Wat2WasmModuleMachineFuncGroup_0.func_1431(n, memory, instance);
                return;
            }
            case 1433: {
                Wat2WasmModuleMachineFuncGroup_0.func_1433(n, memory, instance);
                return;
            }
            case 1435: {
                Wat2WasmModuleMachineFuncGroup_0.func_1435(n, memory, instance);
                return;
            }
            case 1437: {
                Wat2WasmModuleMachineFuncGroup_0.func_1437(n, memory, instance);
                return;
            }
            case 1439: {
                Wat2WasmModuleMachineFuncGroup_0.func_1439(n, memory, instance);
                return;
            }
            case 1441: {
                Wat2WasmModuleMachineFuncGroup_0.func_1441(n, memory, instance);
                return;
            }
            case 1443: {
                Wat2WasmModuleMachineFuncGroup_0.func_1443(n, memory, instance);
                return;
            }
            case 1446: {
                Wat2WasmModuleMachineFuncGroup_0.func_1446(n, memory, instance);
                return;
            }
            case 1448: {
                Wat2WasmModuleMachineFuncGroup_0.func_1448(n, memory, instance);
                return;
            }
            case 1450: {
                Wat2WasmModuleMachineFuncGroup_0.func_1450(n, memory, instance);
                return;
            }
            case 1452: {
                Wat2WasmModuleMachineFuncGroup_0.func_1452(n, memory, instance);
                return;
            }
            case 1454: {
                Wat2WasmModuleMachineFuncGroup_0.func_1454(n, memory, instance);
                return;
            }
            case 1455: {
                Wat2WasmModuleMachineFuncGroup_0.func_1455(n, memory, instance);
                return;
            }
            case 1456: {
                Wat2WasmModuleMachineFuncGroup_0.func_1456(n, memory, instance);
                return;
            }
            case 1457: {
                Wat2WasmModuleMachineFuncGroup_0.func_1457(n, memory, instance);
                return;
            }
            case 1459: {
                Wat2WasmModuleMachineFuncGroup_0.func_1459(n, memory, instance);
                return;
            }
            case 1461: {
                Wat2WasmModuleMachineFuncGroup_0.func_1461(n, memory, instance);
                return;
            }
            case 1463: {
                Wat2WasmModuleMachineFuncGroup_0.func_1463(n, memory, instance);
                return;
            }
            case 1464: {
                Wat2WasmModuleMachineFuncGroup_0.func_1464(n, memory, instance);
                return;
            }
            case 1466: {
                Wat2WasmModuleMachineFuncGroup_0.func_1466(n, memory, instance);
                return;
            }
            case 1468: {
                Wat2WasmModuleMachineFuncGroup_0.func_1468(n, memory, instance);
                return;
            }
            case 1470: {
                Wat2WasmModuleMachineFuncGroup_0.func_1470(n, memory, instance);
                return;
            }
            case 1472: {
                Wat2WasmModuleMachineFuncGroup_0.func_1472(n, memory, instance);
                return;
            }
            case 1474: {
                Wat2WasmModuleMachineFuncGroup_0.func_1474(n, memory, instance);
                return;
            }
            case 1476: {
                Wat2WasmModuleMachineFuncGroup_0.func_1476(n, memory, instance);
                return;
            }
            case 1478: {
                Wat2WasmModuleMachineFuncGroup_0.func_1478(n, memory, instance);
                return;
            }
            case 1480: {
                Wat2WasmModuleMachineFuncGroup_0.func_1480(n, memory, instance);
                return;
            }
            case 1482: {
                Wat2WasmModuleMachineFuncGroup_0.func_1482(n, memory, instance);
                return;
            }
            case 1483: {
                Wat2WasmModuleMachineFuncGroup_0.func_1483(n, memory, instance);
                return;
            }
            case 1485: {
                Wat2WasmModuleMachineFuncGroup_0.func_1485(n, memory, instance);
                return;
            }
            case 1486: {
                Wat2WasmModuleMachineFuncGroup_0.func_1486(n, memory, instance);
                return;
            }
            case 1488: {
                Wat2WasmModuleMachineFuncGroup_0.func_1488(n, memory, instance);
                return;
            }
            case 1489: {
                Wat2WasmModuleMachineFuncGroup_0.func_1489(n, memory, instance);
                return;
            }
            case 1490: {
                Wat2WasmModuleMachineFuncGroup_0.func_1490(n, memory, instance);
                return;
            }
            case 1491: {
                Wat2WasmModuleMachineFuncGroup_0.func_1491(n, memory, instance);
                return;
            }
            case 1492: {
                Wat2WasmModuleMachineFuncGroup_0.func_1492(n, memory, instance);
                return;
            }
            case 1494: {
                Wat2WasmModuleMachineFuncGroup_0.func_1494(n, memory, instance);
                return;
            }
            case 1496: {
                Wat2WasmModuleMachineFuncGroup_0.func_1496(n, memory, instance);
                return;
            }
            case 1498: {
                Wat2WasmModuleMachineFuncGroup_0.func_1498(n, memory, instance);
                return;
            }
            case 1500: {
                Wat2WasmModuleMachineFuncGroup_0.func_1500(n, memory, instance);
                return;
            }
            case 1502: {
                Wat2WasmModuleMachineFuncGroup_0.func_1502(n, memory, instance);
                return;
            }
            case 1504: {
                Wat2WasmModuleMachineFuncGroup_0.func_1504(n, memory, instance);
                return;
            }
            case 1506: {
                Wat2WasmModuleMachineFuncGroup_0.func_1506(n, memory, instance);
                return;
            }
            case 1508: {
                Wat2WasmModuleMachineFuncGroup_0.func_1508(n, memory, instance);
                return;
            }
            case 1510: {
                Wat2WasmModuleMachineFuncGroup_0.func_1510(n, memory, instance);
                return;
            }
            case 1512: {
                Wat2WasmModuleMachineFuncGroup_0.func_1512(n, memory, instance);
                return;
            }
            case 1514: {
                Wat2WasmModuleMachineFuncGroup_0.func_1514(n, memory, instance);
                return;
            }
            case 1516: {
                Wat2WasmModuleMachineFuncGroup_0.func_1516(n, memory, instance);
                return;
            }
            case 1518: {
                Wat2WasmModuleMachineFuncGroup_0.func_1518(n, memory, instance);
                return;
            }
            case 1519: {
                Wat2WasmModuleMachineFuncGroup_0.func_1519(n, memory, instance);
                return;
            }
            case 1520: {
                Wat2WasmModuleMachineFuncGroup_0.func_1520(n, memory, instance);
                return;
            }
            case 1522: {
                Wat2WasmModuleMachineFuncGroup_0.func_1522(n, memory, instance);
                return;
            }
            case 1523: {
                Wat2WasmModuleMachineFuncGroup_0.func_1523(n, memory, instance);
                return;
            }
            case 1525: {
                Wat2WasmModuleMachineFuncGroup_0.func_1525(n, memory, instance);
                return;
            }
            case 1527: {
                Wat2WasmModuleMachineFuncGroup_0.func_1527(n, memory, instance);
                return;
            }
            case 1528: {
                Wat2WasmModuleMachineFuncGroup_0.func_1528(n, memory, instance);
                return;
            }
            case 1530: {
                Wat2WasmModuleMachineFuncGroup_0.func_1530(n, memory, instance);
                return;
            }
            case 1531: {
                Wat2WasmModuleMachineFuncGroup_0.func_1531(n, memory, instance);
                return;
            }
            case 1533: {
                Wat2WasmModuleMachineFuncGroup_0.func_1533(n, memory, instance);
                return;
            }
            case 1534: {
                Wat2WasmModuleMachineFuncGroup_0.func_1534(n, memory, instance);
                return;
            }
            case 1536: {
                Wat2WasmModuleMachineFuncGroup_0.func_1536(n, memory, instance);
                return;
            }
            case 1537: {
                Wat2WasmModuleMachineFuncGroup_0.func_1537(n, memory, instance);
                return;
            }
            case 1539: {
                Wat2WasmModuleMachineFuncGroup_0.func_1539(n, memory, instance);
                return;
            }
            case 1540: {
                Wat2WasmModuleMachineFuncGroup_0.func_1540(n, memory, instance);
                return;
            }
            case 1542: {
                Wat2WasmModuleMachineFuncGroup_0.func_1542(n, memory, instance);
                return;
            }
            case 1543: {
                Wat2WasmModuleMachineFuncGroup_0.func_1543(n, memory, instance);
                return;
            }
            case 1544: {
                Wat2WasmModuleMachineFuncGroup_0.func_1544(n, memory, instance);
                return;
            }
            case 1546: {
                Wat2WasmModuleMachineFuncGroup_0.func_1546(n, memory, instance);
                return;
            }
            case 1547: {
                Wat2WasmModuleMachineFuncGroup_0.func_1547(n, memory, instance);
                return;
            }
            case 1549: {
                Wat2WasmModuleMachineFuncGroup_0.func_1549(n, memory, instance);
                return;
            }
            case 1550: {
                Wat2WasmModuleMachineFuncGroup_0.func_1550(n, memory, instance);
                return;
            }
            case 1552: {
                Wat2WasmModuleMachineFuncGroup_0.func_1552(n, memory, instance);
                return;
            }
            case 1554: {
                Wat2WasmModuleMachineFuncGroup_0.func_1554(n, memory, instance);
                return;
            }
            case 1556: {
                Wat2WasmModuleMachineFuncGroup_0.func_1556(n, memory, instance);
                return;
            }
            case 1558: {
                Wat2WasmModuleMachineFuncGroup_0.func_1558(n, memory, instance);
                return;
            }
            case 1560: {
                Wat2WasmModuleMachineFuncGroup_0.func_1560(n, memory, instance);
                return;
            }
            case 1562: {
                Wat2WasmModuleMachineFuncGroup_0.func_1562(n, memory, instance);
                return;
            }
            case 1564: {
                Wat2WasmModuleMachineFuncGroup_0.func_1564(n, memory, instance);
                return;
            }
            case 1567: {
                Wat2WasmModuleMachineFuncGroup_0.func_1567(n, memory, instance);
                return;
            }
            case 1570: {
                Wat2WasmModuleMachineFuncGroup_0.func_1570(n, memory, instance);
                return;
            }
            case 1574: {
                Wat2WasmModuleMachineFuncGroup_0.func_1574(n, memory, instance);
                return;
            }
            case 1576: {
                Wat2WasmModuleMachineFuncGroup_0.func_1576(n, memory, instance);
                return;
            }
            case 1591: {
                Wat2WasmModuleMachineFuncGroup_0.func_1591(n, memory, instance);
                return;
            }
            case 1602: {
                Wat2WasmModuleMachineFuncGroup_0.func_1602(n, memory, instance);
                return;
            }
            case 1612: {
                Wat2WasmModuleMachineFuncGroup_0.func_1612(n, memory, instance);
                return;
            }
            case 1618: {
                Wat2WasmModuleMachineFuncGroup_0.func_1618(n, memory, instance);
                return;
            }
            case 1632: {
                Wat2WasmModuleMachineFuncGroup_0.func_1632(n, memory, instance);
                return;
            }
            case 1655: {
                Wat2WasmModuleMachineFuncGroup_0.func_1655(n, memory, instance);
                return;
            }
            case 1712: {
                Wat2WasmModuleMachineFuncGroup_0.func_1712(n, memory, instance);
                return;
            }
            case 1742: {
                Wat2WasmModuleMachineFuncGroup_0.func_1742(n, memory, instance);
                return;
            }
            case 1743: {
                Wat2WasmModuleMachineFuncGroup_0.func_1743(n, memory, instance);
                return;
            }
            case 1745: {
                Wat2WasmModuleMachineFuncGroup_0.func_1745(n, memory, instance);
                return;
            }
            case 1747: {
                Wat2WasmModuleMachineFuncGroup_0.func_1747(n, memory, instance);
                return;
            }
            case 1749: {
                Wat2WasmModuleMachineFuncGroup_0.func_1749(n, memory, instance);
                return;
            }
            case 1751: {
                Wat2WasmModuleMachineFuncGroup_0.func_1751(n, memory, instance);
                return;
            }
            case 1777: {
                Wat2WasmModuleMachineFuncGroup_0.func_1777(n, memory, instance);
                return;
            }
            case 1781: {
                Wat2WasmModuleMachineFuncGroup_0.func_1781(n, memory, instance);
                return;
            }
            case 1784: {
                Wat2WasmModuleMachineFuncGroup_0.func_1784(n, memory, instance);
                return;
            }
            case 1785: {
                Wat2WasmModuleMachineFuncGroup_0.func_1785(n, memory, instance);
                return;
            }
            case 1790: {
                Wat2WasmModuleMachineFuncGroup_0.func_1790(n, memory, instance);
                return;
            }
            case 1806: {
                Wat2WasmModuleMachineFuncGroup_0.func_1806(n, memory, instance);
                return;
            }
            case 1827: {
                Wat2WasmModuleMachineFuncGroup_0.func_1827(n, memory, instance);
                return;
            }
            case 1830: {
                Wat2WasmModuleMachineFuncGroup_0.func_1830(n, memory, instance);
                return;
            }
            case 1862: {
                Wat2WasmModuleMachineFuncGroup_0.func_1862(n, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_1(final int n, final int n2, final int n3, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n3);
        final int requiredRef = table.requiredRef(n2);
        final Instance instance2 = table.instance(n2);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n }, 1, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 4: {
                return Wat2WasmModuleMachineFuncGroup_0.func_4(n, memory, instance);
            }
            case 32: {
                return Wat2WasmModuleMachineFuncGroup_0.func_32(n, memory, instance);
            }
            case 41: {
                return Wat2WasmModuleMachineFuncGroup_0.func_41(n, memory, instance);
            }
            case 78: {
                return Wat2WasmModuleMachineFuncGroup_0.func_78(n, memory, instance);
            }
            case 79: {
                return Wat2WasmModuleMachineFuncGroup_0.func_79(n, memory, instance);
            }
            case 80: {
                return Wat2WasmModuleMachineFuncGroup_0.func_80(n, memory, instance);
            }
            case 130: {
                return Wat2WasmModuleMachineFuncGroup_0.func_130(n, memory, instance);
            }
            case 154: {
                return Wat2WasmModuleMachineFuncGroup_0.func_154(n, memory, instance);
            }
            case 162: {
                return Wat2WasmModuleMachineFuncGroup_0.func_162(n, memory, instance);
            }
            case 167: {
                return Wat2WasmModuleMachineFuncGroup_0.func_167(n, memory, instance);
            }
            case 186: {
                return Wat2WasmModuleMachineFuncGroup_0.func_186(n, memory, instance);
            }
            case 195: {
                return Wat2WasmModuleMachineFuncGroup_0.func_195(n, memory, instance);
            }
            case 216: {
                return Wat2WasmModuleMachineFuncGroup_0.func_216(n, memory, instance);
            }
            case 218: {
                return Wat2WasmModuleMachineFuncGroup_0.func_218(n, memory, instance);
            }
            case 228: {
                return Wat2WasmModuleMachineFuncGroup_0.func_228(n, memory, instance);
            }
            case 230: {
                return Wat2WasmModuleMachineFuncGroup_0.func_230(n, memory, instance);
            }
            case 234: {
                return Wat2WasmModuleMachineFuncGroup_0.func_234(n, memory, instance);
            }
            case 242: {
                return Wat2WasmModuleMachineFuncGroup_0.func_242(n, memory, instance);
            }
            case 273: {
                return Wat2WasmModuleMachineFuncGroup_0.func_273(n, memory, instance);
            }
            case 275: {
                return Wat2WasmModuleMachineFuncGroup_0.func_275(n, memory, instance);
            }
            case 286: {
                return Wat2WasmModuleMachineFuncGroup_0.func_286(n, memory, instance);
            }
            case 287: {
                return Wat2WasmModuleMachineFuncGroup_0.func_287(n, memory, instance);
            }
            case 289: {
                return Wat2WasmModuleMachineFuncGroup_0.func_289(n, memory, instance);
            }
            case 314: {
                return Wat2WasmModuleMachineFuncGroup_0.func_314(n, memory, instance);
            }
            case 317: {
                return Wat2WasmModuleMachineFuncGroup_0.func_317(n, memory, instance);
            }
            case 318: {
                return Wat2WasmModuleMachineFuncGroup_0.func_318(n, memory, instance);
            }
            case 331: {
                return Wat2WasmModuleMachineFuncGroup_0.func_331(n, memory, instance);
            }
            case 332: {
                return Wat2WasmModuleMachineFuncGroup_0.func_332(n, memory, instance);
            }
            case 334: {
                return Wat2WasmModuleMachineFuncGroup_0.func_334(n, memory, instance);
            }
            case 364: {
                return Wat2WasmModuleMachineFuncGroup_0.func_364(n, memory, instance);
            }
            case 369: {
                return Wat2WasmModuleMachineFuncGroup_0.func_369(n, memory, instance);
            }
            case 455: {
                return Wat2WasmModuleMachineFuncGroup_0.func_455(n, memory, instance);
            }
            case 456: {
                return Wat2WasmModuleMachineFuncGroup_0.func_456(n, memory, instance);
            }
            case 458: {
                return Wat2WasmModuleMachineFuncGroup_0.func_458(n, memory, instance);
            }
            case 538: {
                return Wat2WasmModuleMachineFuncGroup_0.func_538(n, memory, instance);
            }
            case 568: {
                return Wat2WasmModuleMachineFuncGroup_0.func_568(n, memory, instance);
            }
            case 642: {
                return Wat2WasmModuleMachineFuncGroup_0.func_642(n, memory, instance);
            }
            case 643: {
                return Wat2WasmModuleMachineFuncGroup_0.func_643(n, memory, instance);
            }
            case 646: {
                return Wat2WasmModuleMachineFuncGroup_0.func_646(n, memory, instance);
            }
            case 649: {
                return Wat2WasmModuleMachineFuncGroup_0.func_649(n, memory, instance);
            }
            case 653: {
                return Wat2WasmModuleMachineFuncGroup_0.func_653(n, memory, instance);
            }
            case 656: {
                return Wat2WasmModuleMachineFuncGroup_0.func_656(n, memory, instance);
            }
            case 659: {
                return Wat2WasmModuleMachineFuncGroup_0.func_659(n, memory, instance);
            }
            case 665: {
                return Wat2WasmModuleMachineFuncGroup_0.func_665(n, memory, instance);
            }
            case 668: {
                return Wat2WasmModuleMachineFuncGroup_0.func_668(n, memory, instance);
            }
            case 671: {
                return Wat2WasmModuleMachineFuncGroup_0.func_671(n, memory, instance);
            }
            case 676: {
                return Wat2WasmModuleMachineFuncGroup_0.func_676(n, memory, instance);
            }
            case 687: {
                return Wat2WasmModuleMachineFuncGroup_0.func_687(n, memory, instance);
            }
            case 689: {
                return Wat2WasmModuleMachineFuncGroup_0.func_689(n, memory, instance);
            }
            case 693: {
                return Wat2WasmModuleMachineFuncGroup_0.func_693(n, memory, instance);
            }
            case 694: {
                return Wat2WasmModuleMachineFuncGroup_0.func_694(n, memory, instance);
            }
            case 695: {
                return Wat2WasmModuleMachineFuncGroup_0.func_695(n, memory, instance);
            }
            case 718: {
                return Wat2WasmModuleMachineFuncGroup_0.func_718(n, memory, instance);
            }
            case 719: {
                return Wat2WasmModuleMachineFuncGroup_0.func_719(n, memory, instance);
            }
            case 723: {
                return Wat2WasmModuleMachineFuncGroup_0.func_723(n, memory, instance);
            }
            case 728: {
                return Wat2WasmModuleMachineFuncGroup_0.func_728(n, memory, instance);
            }
            case 729: {
                return Wat2WasmModuleMachineFuncGroup_0.func_729(n, memory, instance);
            }
            case 734: {
                return Wat2WasmModuleMachineFuncGroup_0.func_734(n, memory, instance);
            }
            case 743: {
                return Wat2WasmModuleMachineFuncGroup_0.func_743(n, memory, instance);
            }
            case 750: {
                return Wat2WasmModuleMachineFuncGroup_0.func_750(n, memory, instance);
            }
            case 753: {
                return Wat2WasmModuleMachineFuncGroup_0.func_753(n, memory, instance);
            }
            case 759: {
                return Wat2WasmModuleMachineFuncGroup_0.func_759(n, memory, instance);
            }
            case 761: {
                return Wat2WasmModuleMachineFuncGroup_0.func_761(n, memory, instance);
            }
            case 766: {
                return Wat2WasmModuleMachineFuncGroup_0.func_766(n, memory, instance);
            }
            case 769: {
                return Wat2WasmModuleMachineFuncGroup_0.func_769(n, memory, instance);
            }
            case 775: {
                return Wat2WasmModuleMachineFuncGroup_0.func_775(n, memory, instance);
            }
            case 777: {
                return Wat2WasmModuleMachineFuncGroup_0.func_777(n, memory, instance);
            }
            case 781: {
                return Wat2WasmModuleMachineFuncGroup_0.func_781(n, memory, instance);
            }
            case 784: {
                return Wat2WasmModuleMachineFuncGroup_0.func_784(n, memory, instance);
            }
            case 786: {
                return Wat2WasmModuleMachineFuncGroup_0.func_786(n, memory, instance);
            }
            case 800: {
                return Wat2WasmModuleMachineFuncGroup_0.func_800(n, memory, instance);
            }
            case 839: {
                return Wat2WasmModuleMachineFuncGroup_0.func_839(n, memory, instance);
            }
            case 840: {
                return Wat2WasmModuleMachineFuncGroup_0.func_840(n, memory, instance);
            }
            case 846: {
                return Wat2WasmModuleMachineFuncGroup_0.func_846(n, memory, instance);
            }
            case 849: {
                return Wat2WasmModuleMachineFuncGroup_0.func_849(n, memory, instance);
            }
            case 857: {
                return Wat2WasmModuleMachineFuncGroup_0.func_857(n, memory, instance);
            }
            case 867: {
                return Wat2WasmModuleMachineFuncGroup_0.func_867(n, memory, instance);
            }
            case 871: {
                return Wat2WasmModuleMachineFuncGroup_0.func_871(n, memory, instance);
            }
            case 875: {
                return Wat2WasmModuleMachineFuncGroup_0.func_875(n, memory, instance);
            }
            case 879: {
                return Wat2WasmModuleMachineFuncGroup_0.func_879(n, memory, instance);
            }
            case 887: {
                return Wat2WasmModuleMachineFuncGroup_0.func_887(n, memory, instance);
            }
            case 891: {
                return Wat2WasmModuleMachineFuncGroup_0.func_891(n, memory, instance);
            }
            case 894: {
                return Wat2WasmModuleMachineFuncGroup_0.func_894(n, memory, instance);
            }
            case 901: {
                return Wat2WasmModuleMachineFuncGroup_0.func_901(n, memory, instance);
            }
            case 903: {
                return Wat2WasmModuleMachineFuncGroup_0.func_903(n, memory, instance);
            }
            case 932: {
                return Wat2WasmModuleMachineFuncGroup_0.func_932(n, memory, instance);
            }
            case 935: {
                return Wat2WasmModuleMachineFuncGroup_0.func_935(n, memory, instance);
            }
            case 939: {
                return Wat2WasmModuleMachineFuncGroup_0.func_939(n, memory, instance);
            }
            case 940: {
                return Wat2WasmModuleMachineFuncGroup_0.func_940(n, memory, instance);
            }
            case 941: {
                return Wat2WasmModuleMachineFuncGroup_0.func_941(n, memory, instance);
            }
            case 971: {
                return Wat2WasmModuleMachineFuncGroup_0.func_971(n, memory, instance);
            }
            case 972: {
                return Wat2WasmModuleMachineFuncGroup_0.func_972(n, memory, instance);
            }
            case 974: {
                return Wat2WasmModuleMachineFuncGroup_0.func_974(n, memory, instance);
            }
            case 978: {
                return Wat2WasmModuleMachineFuncGroup_0.func_978(n, memory, instance);
            }
            case 981: {
                return Wat2WasmModuleMachineFuncGroup_0.func_981(n, memory, instance);
            }
            case 987: {
                return Wat2WasmModuleMachineFuncGroup_0.func_987(n, memory, instance);
            }
            case 989: {
                return Wat2WasmModuleMachineFuncGroup_0.func_989(n, memory, instance);
            }
            case 1008: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1008(n, memory, instance);
            }
            case 1016: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1016(n, memory, instance);
            }
            case 1019: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1019(n, memory, instance);
            }
            case 1038: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1038(n, memory, instance);
            }
            case 1042: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1042(n, memory, instance);
            }
            case 1051: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1051(n, memory, instance);
            }
            case 1055: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1055(n, memory, instance);
            }
            case 1059: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1059(n, memory, instance);
            }
            case 1075: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1075(n, memory, instance);
            }
            case 1079: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1079(n, memory, instance);
            }
            case 1085: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1085(n, memory, instance);
            }
            case 1087: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1087(n, memory, instance);
            }
            case 1091: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1091(n, memory, instance);
            }
            case 1102: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1102(n, memory, instance);
            }
            case 1156: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1156(n, memory, instance);
            }
            case 1157: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1157(n, memory, instance);
            }
            case 1169: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1169(n, memory, instance);
            }
            case 1211: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1211(n, memory, instance);
            }
            case 1239: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1239(n, memory, instance);
            }
            case 1241: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1241(n, memory, instance);
            }
            case 1244: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1244(n, memory, instance);
            }
            case 1245: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1245(n, memory, instance);
            }
            case 1246: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1246(n, memory, instance);
            }
            case 1247: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1247(n, memory, instance);
            }
            case 1248: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1248(n, memory, instance);
            }
            case 1250: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1250(n, memory, instance);
            }
            case 1251: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1251(n, memory, instance);
            }
            case 1253: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1253(n, memory, instance);
            }
            case 1256: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1256(n, memory, instance);
            }
            case 1262: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1262(n, memory, instance);
            }
            case 1264: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1264(n, memory, instance);
            }
            case 1266: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1266(n, memory, instance);
            }
            case 1299: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1299(n, memory, instance);
            }
            case 1305: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1305(n, memory, instance);
            }
            case 1311: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1311(n, memory, instance);
            }
            case 1313: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1313(n, memory, instance);
            }
            case 1315: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1315(n, memory, instance);
            }
            case 1392: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1392(n, memory, instance);
            }
            case 1393: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1393(n, memory, instance);
            }
            case 1396: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1396(n, memory, instance);
            }
            case 1399: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1399(n, memory, instance);
            }
            case 1401: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1401(n, memory, instance);
            }
            case 1404: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1404(n, memory, instance);
            }
            case 1410: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1410(n, memory, instance);
            }
            case 1414: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1414(n, memory, instance);
            }
            case 1416: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1416(n, memory, instance);
            }
            case 1418: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1418(n, memory, instance);
            }
            case 1420: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1420(n, memory, instance);
            }
            case 1423: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1423(n, memory, instance);
            }
            case 1425: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1425(n, memory, instance);
            }
            case 1427: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1427(n, memory, instance);
            }
            case 1429: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1429(n, memory, instance);
            }
            case 1430: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1430(n, memory, instance);
            }
            case 1432: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1432(n, memory, instance);
            }
            case 1434: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1434(n, memory, instance);
            }
            case 1436: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1436(n, memory, instance);
            }
            case 1438: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1438(n, memory, instance);
            }
            case 1440: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1440(n, memory, instance);
            }
            case 1442: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1442(n, memory, instance);
            }
            case 1444: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1444(n, memory, instance);
            }
            case 1445: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1445(n, memory, instance);
            }
            case 1447: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1447(n, memory, instance);
            }
            case 1449: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1449(n, memory, instance);
            }
            case 1451: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1451(n, memory, instance);
            }
            case 1453: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1453(n, memory, instance);
            }
            case 1458: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1458(n, memory, instance);
            }
            case 1460: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1460(n, memory, instance);
            }
            case 1462: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1462(n, memory, instance);
            }
            case 1465: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1465(n, memory, instance);
            }
            case 1467: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1467(n, memory, instance);
            }
            case 1469: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1469(n, memory, instance);
            }
            case 1471: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1471(n, memory, instance);
            }
            case 1473: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1473(n, memory, instance);
            }
            case 1475: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1475(n, memory, instance);
            }
            case 1477: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1477(n, memory, instance);
            }
            case 1479: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1479(n, memory, instance);
            }
            case 1481: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1481(n, memory, instance);
            }
            case 1484: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1484(n, memory, instance);
            }
            case 1487: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1487(n, memory, instance);
            }
            case 1493: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1493(n, memory, instance);
            }
            case 1495: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1495(n, memory, instance);
            }
            case 1497: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1497(n, memory, instance);
            }
            case 1499: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1499(n, memory, instance);
            }
            case 1501: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1501(n, memory, instance);
            }
            case 1503: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1503(n, memory, instance);
            }
            case 1505: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1505(n, memory, instance);
            }
            case 1507: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1507(n, memory, instance);
            }
            case 1509: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1509(n, memory, instance);
            }
            case 1511: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1511(n, memory, instance);
            }
            case 1513: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1513(n, memory, instance);
            }
            case 1515: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1515(n, memory, instance);
            }
            case 1517: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1517(n, memory, instance);
            }
            case 1521: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1521(n, memory, instance);
            }
            case 1524: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1524(n, memory, instance);
            }
            case 1526: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1526(n, memory, instance);
            }
            case 1529: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1529(n, memory, instance);
            }
            case 1532: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1532(n, memory, instance);
            }
            case 1535: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1535(n, memory, instance);
            }
            case 1538: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1538(n, memory, instance);
            }
            case 1541: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1541(n, memory, instance);
            }
            case 1545: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1545(n, memory, instance);
            }
            case 1548: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1548(n, memory, instance);
            }
            case 1551: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1551(n, memory, instance);
            }
            case 1553: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1553(n, memory, instance);
            }
            case 1555: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1555(n, memory, instance);
            }
            case 1557: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1557(n, memory, instance);
            }
            case 1559: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1559(n, memory, instance);
            }
            case 1561: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1561(n, memory, instance);
            }
            case 1563: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1563(n, memory, instance);
            }
            case 1565: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1565(n, memory, instance);
            }
            case 1566: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1566(n, memory, instance);
            }
            case 1568: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1568(n, memory, instance);
            }
            case 1569: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1569(n, memory, instance);
            }
            case 1571: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1571(n, memory, instance);
            }
            case 1572: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1572(n, memory, instance);
            }
            case 1579: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1579(n, memory, instance);
            }
            case 1588: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1588(n, memory, instance);
            }
            case 1590: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1590(n, memory, instance);
            }
            case 1593: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1593(n, memory, instance);
            }
            case 1594: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1594(n, memory, instance);
            }
            case 1595: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1595(n, memory, instance);
            }
            case 1599: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1599(n, memory, instance);
            }
            case 1600: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1600(n, memory, instance);
            }
            case 1601: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1601(n, memory, instance);
            }
            case 1604: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1604(n, memory, instance);
            }
            case 1605: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1605(n, memory, instance);
            }
            case 1606: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1606(n, memory, instance);
            }
            case 1611: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1611(n, memory, instance);
            }
            case 1614: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1614(n, memory, instance);
            }
            case 1615: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1615(n, memory, instance);
            }
            case 1624: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1624(n, memory, instance);
            }
            case 1625: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1625(n, memory, instance);
            }
            case 1626: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1626(n, memory, instance);
            }
            case 1627: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1627(n, memory, instance);
            }
            case 1629: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1629(n, memory, instance);
            }
            case 1630: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1630(n, memory, instance);
            }
            case 1633: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1633(n, memory, instance);
            }
            case 1634: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1634(n, memory, instance);
            }
            case 1636: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1636(n, memory, instance);
            }
            case 1637: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1637(n, memory, instance);
            }
            case 1639: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1639(n, memory, instance);
            }
            case 1640: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1640(n, memory, instance);
            }
            case 1641: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1641(n, memory, instance);
            }
            case 1656: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1656(n, memory, instance);
            }
            case 1672: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1672(n, memory, instance);
            }
            case 1673: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1673(n, memory, instance);
            }
            case 1674: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1674(n, memory, instance);
            }
            case 1675: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1675(n, memory, instance);
            }
            case 1676: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1676(n, memory, instance);
            }
            case 1678: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1678(n, memory, instance);
            }
            case 1679: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1679(n, memory, instance);
            }
            case 1681: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1681(n, memory, instance);
            }
            case 1683: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1683(n, memory, instance);
            }
            case 1684: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1684(n, memory, instance);
            }
            case 1690: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1690(n, memory, instance);
            }
            case 1697: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1697(n, memory, instance);
            }
            case 1701: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1701(n, memory, instance);
            }
            case 1703: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1703(n, memory, instance);
            }
            case 1713: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1713(n, memory, instance);
            }
            case 1715: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1715(n, memory, instance);
            }
            case 1716: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1716(n, memory, instance);
            }
            case 1717: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1717(n, memory, instance);
            }
            case 1720: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1720(n, memory, instance);
            }
            case 1721: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1721(n, memory, instance);
            }
            case 1723: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1723(n, memory, instance);
            }
            case 1725: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1725(n, memory, instance);
            }
            case 1740: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1740(n, memory, instance);
            }
            case 1741: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1741(n, memory, instance);
            }
            case 1744: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1744(n, memory, instance);
            }
            case 1746: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1746(n, memory, instance);
            }
            case 1748: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1748(n, memory, instance);
            }
            case 1750: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1750(n, memory, instance);
            }
            case 1755: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1755(n, memory, instance);
            }
            case 1776: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1776(n, memory, instance);
            }
            case 1782: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1782(n, memory, instance);
            }
            case 1783: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1783(n, memory, instance);
            }
            case 1796: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1796(n, memory, instance);
            }
            case 1808: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1808(n, memory, instance);
            }
            case 1811: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1811(n, memory, instance);
            }
            case 1818: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1818(n, memory, instance);
            }
            case 1819: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1819(n, memory, instance);
            }
            case 1822: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1822(n, memory, instance);
            }
            case 1823: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1823(n, memory, instance);
            }
            case 1831: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1831(n, memory, instance);
            }
            case 1832: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1832(n, memory, instance);
            }
            case 1833: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1833(n, memory, instance);
            }
            case 1834: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1834(n, memory, instance);
            }
            case 1835: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1835(n, memory, instance);
            }
            case 1844: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1844(n, memory, instance);
            }
            case 1850: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1850(n, memory, instance);
            }
            case 1853: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1853(n, memory, instance);
            }
            case 1854: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1854(n, memory, instance);
            }
            case 1859: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1859(n, memory, instance);
            }
            case 1864: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1864(n, memory, instance);
            }
            case 1877: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1877(n, memory, instance);
            }
            case 1878: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1878(n, memory, instance);
            }
            case 1880: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1880(n, memory, instance);
            }
            case 1896: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1896(n, memory, instance);
            }
            case 1897: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1897(n, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_2(final int n, final int n2, final int n3, final int n4, final int n5, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n5);
        final int requiredRef = table.requiredRef(n4);
        final Instance instance2 = table.instance(n4);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3 }, 2, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 37: {
                Wat2WasmModuleMachineFuncGroup_0.func_37(n, n2, n3, memory, instance);
                return;
            }
            case 39: {
                Wat2WasmModuleMachineFuncGroup_0.func_39(n, n2, n3, memory, instance);
                return;
            }
            case 51: {
                Wat2WasmModuleMachineFuncGroup_0.func_51(n, n2, n3, memory, instance);
                return;
            }
            case 53: {
                Wat2WasmModuleMachineFuncGroup_0.func_53(n, n2, n3, memory, instance);
                return;
            }
            case 55: {
                Wat2WasmModuleMachineFuncGroup_0.func_55(n, n2, n3, memory, instance);
                return;
            }
            case 64: {
                Wat2WasmModuleMachineFuncGroup_0.func_64(n, n2, n3, memory, instance);
                return;
            }
            case 115: {
                Wat2WasmModuleMachineFuncGroup_0.func_115(n, n2, n3, memory, instance);
                return;
            }
            case 117: {
                Wat2WasmModuleMachineFuncGroup_0.func_117(n, n2, n3, memory, instance);
                return;
            }
            case 152: {
                Wat2WasmModuleMachineFuncGroup_0.func_152(n, n2, n3, memory, instance);
                return;
            }
            case 169: {
                Wat2WasmModuleMachineFuncGroup_0.func_169(n, n2, n3, memory, instance);
                return;
            }
            case 170: {
                Wat2WasmModuleMachineFuncGroup_0.func_170(n, n2, n3, memory, instance);
                return;
            }
            case 173: {
                Wat2WasmModuleMachineFuncGroup_0.func_173(n, n2, n3, memory, instance);
                return;
            }
            case 176: {
                Wat2WasmModuleMachineFuncGroup_0.func_176(n, n2, n3, memory, instance);
                return;
            }
            case 205: {
                Wat2WasmModuleMachineFuncGroup_0.func_205(n, n2, n3, memory, instance);
                return;
            }
            case 211: {
                Wat2WasmModuleMachineFuncGroup_0.func_211(n, n2, n3, memory, instance);
                return;
            }
            case 212: {
                Wat2WasmModuleMachineFuncGroup_0.func_212(n, n2, n3, memory, instance);
                return;
            }
            case 223: {
                Wat2WasmModuleMachineFuncGroup_0.func_223(n, n2, n3, memory, instance);
                return;
            }
            case 246: {
                Wat2WasmModuleMachineFuncGroup_0.func_246(n, n2, n3, memory, instance);
                return;
            }
            case 254: {
                Wat2WasmModuleMachineFuncGroup_0.func_254(n, n2, n3, memory, instance);
                return;
            }
            case 344: {
                Wat2WasmModuleMachineFuncGroup_0.func_344(n, n2, n3, memory, instance);
                return;
            }
            case 545: {
                Wat2WasmModuleMachineFuncGroup_0.func_545(n, n2, n3, memory, instance);
                return;
            }
            case 561: {
                Wat2WasmModuleMachineFuncGroup_0.func_561(n, n2, n3, memory, instance);
                return;
            }
            case 567: {
                Wat2WasmModuleMachineFuncGroup_0.func_567(n, n2, n3, memory, instance);
                return;
            }
            case 573: {
                Wat2WasmModuleMachineFuncGroup_0.func_573(n, n2, n3, memory, instance);
                return;
            }
            case 802: {
                Wat2WasmModuleMachineFuncGroup_0.func_802(n, n2, n3, memory, instance);
                return;
            }
            case 853: {
                Wat2WasmModuleMachineFuncGroup_0.func_853(n, n2, n3, memory, instance);
                return;
            }
            case 862: {
                Wat2WasmModuleMachineFuncGroup_0.func_862(n, n2, n3, memory, instance);
                return;
            }
            case 900: {
                Wat2WasmModuleMachineFuncGroup_0.func_900(n, n2, n3, memory, instance);
                return;
            }
            case 925: {
                Wat2WasmModuleMachineFuncGroup_0.func_925(n, n2, n3, memory, instance);
                return;
            }
            case 1023: {
                Wat2WasmModuleMachineFuncGroup_0.func_1023(n, n2, n3, memory, instance);
                return;
            }
            case 1027: {
                Wat2WasmModuleMachineFuncGroup_0.func_1027(n, n2, n3, memory, instance);
                return;
            }
            case 1099: {
                Wat2WasmModuleMachineFuncGroup_0.func_1099(n, n2, n3, memory, instance);
                return;
            }
            case 1101: {
                Wat2WasmModuleMachineFuncGroup_0.func_1101(n, n2, n3, memory, instance);
                return;
            }
            case 1215: {
                Wat2WasmModuleMachineFuncGroup_0.func_1215(n, n2, n3, memory, instance);
                return;
            }
            case 1234: {
                Wat2WasmModuleMachineFuncGroup_0.func_1234(n, n2, n3, memory, instance);
                return;
            }
            case 1304: {
                Wat2WasmModuleMachineFuncGroup_0.func_1304(n, n2, n3, memory, instance);
                return;
            }
            case 1320: {
                Wat2WasmModuleMachineFuncGroup_0.func_1320(n, n2, n3, memory, instance);
                return;
            }
            case 1616: {
                Wat2WasmModuleMachineFuncGroup_0.func_1616(n, n2, n3, memory, instance);
                return;
            }
            case 1620: {
                Wat2WasmModuleMachineFuncGroup_0.func_1620(n, n2, n3, memory, instance);
                return;
            }
            case 1642: {
                Wat2WasmModuleMachineFuncGroup_0.func_1642(n, n2, n3, memory, instance);
                return;
            }
            case 1646: {
                Wat2WasmModuleMachineFuncGroup_0.func_1646(n, n2, n3, memory, instance);
                return;
            }
            case 1647: {
                Wat2WasmModuleMachineFuncGroup_0.func_1647(n, n2, n3, memory, instance);
                return;
            }
            case 1689: {
                Wat2WasmModuleMachineFuncGroup_0.func_1689(n, n2, n3, memory, instance);
                return;
            }
            case 1694: {
                Wat2WasmModuleMachineFuncGroup_0.func_1694(n, n2, n3, memory, instance);
                return;
            }
            case 1695: {
                Wat2WasmModuleMachineFuncGroup_0.func_1695(n, n2, n3, memory, instance);
                return;
            }
            case 1704: {
                Wat2WasmModuleMachineFuncGroup_0.func_1704(n, n2, n3, memory, instance);
                return;
            }
            case 1708: {
                Wat2WasmModuleMachineFuncGroup_0.func_1708(n, n2, n3, memory, instance);
                return;
            }
            case 1709: {
                Wat2WasmModuleMachineFuncGroup_0.func_1709(n, n2, n3, memory, instance);
                return;
            }
            case 1719: {
                Wat2WasmModuleMachineFuncGroup_0.func_1719(n, n2, n3, memory, instance);
                return;
            }
            case 1872: {
                Wat2WasmModuleMachineFuncGroup_0.func_1872(n, n2, n3, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_3(final int n, final int n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2 }, 3, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 25: {
                Wat2WasmModuleMachineFuncGroup_0.func_25(n, n2, memory, instance);
                return;
            }
            case 30: {
                Wat2WasmModuleMachineFuncGroup_0.func_30(n, n2, memory, instance);
                return;
            }
            case 34: {
                Wat2WasmModuleMachineFuncGroup_0.func_34(n, n2, memory, instance);
                return;
            }
            case 36: {
                Wat2WasmModuleMachineFuncGroup_0.func_36(n, n2, memory, instance);
                return;
            }
            case 38: {
                Wat2WasmModuleMachineFuncGroup_0.func_38(n, n2, memory, instance);
                return;
            }
            case 42: {
                Wat2WasmModuleMachineFuncGroup_0.func_42(n, n2, memory, instance);
                return;
            }
            case 43: {
                Wat2WasmModuleMachineFuncGroup_0.func_43(n, n2, memory, instance);
                return;
            }
            case 47: {
                Wat2WasmModuleMachineFuncGroup_0.func_47(n, n2, memory, instance);
                return;
            }
            case 49: {
                Wat2WasmModuleMachineFuncGroup_0.func_49(n, n2, memory, instance);
                return;
            }
            case 50: {
                Wat2WasmModuleMachineFuncGroup_0.func_50(n, n2, memory, instance);
                return;
            }
            case 52: {
                Wat2WasmModuleMachineFuncGroup_0.func_52(n, n2, memory, instance);
                return;
            }
            case 56: {
                Wat2WasmModuleMachineFuncGroup_0.func_56(n, n2, memory, instance);
                return;
            }
            case 57: {
                Wat2WasmModuleMachineFuncGroup_0.func_57(n, n2, memory, instance);
                return;
            }
            case 58: {
                Wat2WasmModuleMachineFuncGroup_0.func_58(n, n2, memory, instance);
                return;
            }
            case 59: {
                Wat2WasmModuleMachineFuncGroup_0.func_59(n, n2, memory, instance);
                return;
            }
            case 60: {
                Wat2WasmModuleMachineFuncGroup_0.func_60(n, n2, memory, instance);
                return;
            }
            case 61: {
                Wat2WasmModuleMachineFuncGroup_0.func_61(n, n2, memory, instance);
                return;
            }
            case 62: {
                Wat2WasmModuleMachineFuncGroup_0.func_62(n, n2, memory, instance);
                return;
            }
            case 65: {
                Wat2WasmModuleMachineFuncGroup_0.func_65(n, n2, memory, instance);
                return;
            }
            case 66: {
                Wat2WasmModuleMachineFuncGroup_0.func_66(n, n2, memory, instance);
                return;
            }
            case 67: {
                Wat2WasmModuleMachineFuncGroup_0.func_67(n, n2, memory, instance);
                return;
            }
            case 68: {
                Wat2WasmModuleMachineFuncGroup_0.func_68(n, n2, memory, instance);
                return;
            }
            case 69: {
                Wat2WasmModuleMachineFuncGroup_0.func_69(n, n2, memory, instance);
                return;
            }
            case 70: {
                Wat2WasmModuleMachineFuncGroup_0.func_70(n, n2, memory, instance);
                return;
            }
            case 72: {
                Wat2WasmModuleMachineFuncGroup_0.func_72(n, n2, memory, instance);
                return;
            }
            case 84: {
                Wat2WasmModuleMachineFuncGroup_0.func_84(n, n2, memory, instance);
                return;
            }
            case 89: {
                Wat2WasmModuleMachineFuncGroup_0.func_89(n, n2, memory, instance);
                return;
            }
            case 110: {
                Wat2WasmModuleMachineFuncGroup_0.func_110(n, n2, memory, instance);
                return;
            }
            case 111: {
                Wat2WasmModuleMachineFuncGroup_0.func_111(n, n2, memory, instance);
                return;
            }
            case 112: {
                Wat2WasmModuleMachineFuncGroup_0.func_112(n, n2, memory, instance);
                return;
            }
            case 113: {
                Wat2WasmModuleMachineFuncGroup_0.func_113(n, n2, memory, instance);
                return;
            }
            case 129: {
                Wat2WasmModuleMachineFuncGroup_0.func_129(n, n2, memory, instance);
                return;
            }
            case 136: {
                Wat2WasmModuleMachineFuncGroup_0.func_136(n, n2, memory, instance);
                return;
            }
            case 139: {
                Wat2WasmModuleMachineFuncGroup_0.func_139(n, n2, memory, instance);
                return;
            }
            case 140: {
                Wat2WasmModuleMachineFuncGroup_0.func_140(n, n2, memory, instance);
                return;
            }
            case 141: {
                Wat2WasmModuleMachineFuncGroup_0.func_141(n, n2, memory, instance);
                return;
            }
            case 142: {
                Wat2WasmModuleMachineFuncGroup_0.func_142(n, n2, memory, instance);
                return;
            }
            case 143: {
                Wat2WasmModuleMachineFuncGroup_0.func_143(n, n2, memory, instance);
                return;
            }
            case 144: {
                Wat2WasmModuleMachineFuncGroup_0.func_144(n, n2, memory, instance);
                return;
            }
            case 145: {
                Wat2WasmModuleMachineFuncGroup_0.func_145(n, n2, memory, instance);
                return;
            }
            case 147: {
                Wat2WasmModuleMachineFuncGroup_0.func_147(n, n2, memory, instance);
                return;
            }
            case 148: {
                Wat2WasmModuleMachineFuncGroup_0.func_148(n, n2, memory, instance);
                return;
            }
            case 149: {
                Wat2WasmModuleMachineFuncGroup_0.func_149(n, n2, memory, instance);
                return;
            }
            case 150: {
                Wat2WasmModuleMachineFuncGroup_0.func_150(n, n2, memory, instance);
                return;
            }
            case 151: {
                Wat2WasmModuleMachineFuncGroup_0.func_151(n, n2, memory, instance);
                return;
            }
            case 153: {
                Wat2WasmModuleMachineFuncGroup_0.func_153(n, n2, memory, instance);
                return;
            }
            case 161: {
                Wat2WasmModuleMachineFuncGroup_0.func_161(n, n2, memory, instance);
                return;
            }
            case 166: {
                Wat2WasmModuleMachineFuncGroup_0.func_166(n, n2, memory, instance);
                return;
            }
            case 191: {
                Wat2WasmModuleMachineFuncGroup_0.func_191(n, n2, memory, instance);
                return;
            }
            case 192: {
                Wat2WasmModuleMachineFuncGroup_0.func_192(n, n2, memory, instance);
                return;
            }
            case 196: {
                Wat2WasmModuleMachineFuncGroup_0.func_196(n, n2, memory, instance);
                return;
            }
            case 199: {
                Wat2WasmModuleMachineFuncGroup_0.func_199(n, n2, memory, instance);
                return;
            }
            case 203: {
                Wat2WasmModuleMachineFuncGroup_0.func_203(n, n2, memory, instance);
                return;
            }
            case 204: {
                Wat2WasmModuleMachineFuncGroup_0.func_204(n, n2, memory, instance);
                return;
            }
            case 209: {
                Wat2WasmModuleMachineFuncGroup_0.func_209(n, n2, memory, instance);
                return;
            }
            case 214: {
                Wat2WasmModuleMachineFuncGroup_0.func_214(n, n2, memory, instance);
                return;
            }
            case 220: {
                Wat2WasmModuleMachineFuncGroup_0.func_220(n, n2, memory, instance);
                return;
            }
            case 226: {
                Wat2WasmModuleMachineFuncGroup_0.func_226(n, n2, memory, instance);
                return;
            }
            case 248: {
                Wat2WasmModuleMachineFuncGroup_0.func_248(n, n2, memory, instance);
                return;
            }
            case 252: {
                Wat2WasmModuleMachineFuncGroup_0.func_252(n, n2, memory, instance);
                return;
            }
            case 336: {
                Wat2WasmModuleMachineFuncGroup_0.func_336(n, n2, memory, instance);
                return;
            }
            case 343: {
                Wat2WasmModuleMachineFuncGroup_0.func_343(n, n2, memory, instance);
                return;
            }
            case 351: {
                Wat2WasmModuleMachineFuncGroup_0.func_351(n, n2, memory, instance);
                return;
            }
            case 362: {
                Wat2WasmModuleMachineFuncGroup_0.func_362(n, n2, memory, instance);
                return;
            }
            case 367: {
                Wat2WasmModuleMachineFuncGroup_0.func_367(n, n2, memory, instance);
                return;
            }
            case 417: {
                Wat2WasmModuleMachineFuncGroup_0.func_417(n, n2, memory, instance);
                return;
            }
            case 449: {
                Wat2WasmModuleMachineFuncGroup_0.func_449(n, n2, memory, instance);
                return;
            }
            case 534: {
                Wat2WasmModuleMachineFuncGroup_0.func_534(n, n2, memory, instance);
                return;
            }
            case 535: {
                Wat2WasmModuleMachineFuncGroup_0.func_535(n, n2, memory, instance);
                return;
            }
            case 536: {
                Wat2WasmModuleMachineFuncGroup_0.func_536(n, n2, memory, instance);
                return;
            }
            case 537: {
                Wat2WasmModuleMachineFuncGroup_0.func_537(n, n2, memory, instance);
                return;
            }
            case 544: {
                Wat2WasmModuleMachineFuncGroup_0.func_544(n, n2, memory, instance);
                return;
            }
            case 557: {
                Wat2WasmModuleMachineFuncGroup_0.func_557(n, n2, memory, instance);
                return;
            }
            case 558: {
                Wat2WasmModuleMachineFuncGroup_0.func_558(n, n2, memory, instance);
                return;
            }
            case 559: {
                Wat2WasmModuleMachineFuncGroup_0.func_559(n, n2, memory, instance);
                return;
            }
            case 560: {
                Wat2WasmModuleMachineFuncGroup_0.func_560(n, n2, memory, instance);
                return;
            }
            case 562: {
                Wat2WasmModuleMachineFuncGroup_0.func_562(n, n2, memory, instance);
                return;
            }
            case 563: {
                Wat2WasmModuleMachineFuncGroup_0.func_563(n, n2, memory, instance);
                return;
            }
            case 565: {
                Wat2WasmModuleMachineFuncGroup_0.func_565(n, n2, memory, instance);
                return;
            }
            case 574: {
                Wat2WasmModuleMachineFuncGroup_0.func_574(n, n2, memory, instance);
                return;
            }
            case 576: {
                Wat2WasmModuleMachineFuncGroup_0.func_576(n, n2, memory, instance);
                return;
            }
            case 809: {
                Wat2WasmModuleMachineFuncGroup_0.func_809(n, n2, memory, instance);
                return;
            }
            case 816: {
                Wat2WasmModuleMachineFuncGroup_0.func_816(n, n2, memory, instance);
                return;
            }
            case 820: {
                Wat2WasmModuleMachineFuncGroup_0.func_820(n, n2, memory, instance);
                return;
            }
            case 822: {
                Wat2WasmModuleMachineFuncGroup_0.func_822(n, n2, memory, instance);
                return;
            }
            case 830: {
                Wat2WasmModuleMachineFuncGroup_0.func_830(n, n2, memory, instance);
                return;
            }
            case 833: {
                Wat2WasmModuleMachineFuncGroup_0.func_833(n, n2, memory, instance);
                return;
            }
            case 834: {
                Wat2WasmModuleMachineFuncGroup_0.func_834(n, n2, memory, instance);
                return;
            }
            case 835: {
                Wat2WasmModuleMachineFuncGroup_0.func_835(n, n2, memory, instance);
                return;
            }
            case 836: {
                Wat2WasmModuleMachineFuncGroup_0.func_836(n, n2, memory, instance);
                return;
            }
            case 837: {
                Wat2WasmModuleMachineFuncGroup_0.func_837(n, n2, memory, instance);
                return;
            }
            case 843: {
                Wat2WasmModuleMachineFuncGroup_0.func_843(n, n2, memory, instance);
                return;
            }
            case 844: {
                Wat2WasmModuleMachineFuncGroup_0.func_844(n, n2, memory, instance);
                return;
            }
            case 855: {
                Wat2WasmModuleMachineFuncGroup_0.func_855(n, n2, memory, instance);
                return;
            }
            case 929: {
                Wat2WasmModuleMachineFuncGroup_0.func_929(n, n2, memory, instance);
                return;
            }
            case 984: {
                Wat2WasmModuleMachineFuncGroup_0.func_984(n, n2, memory, instance);
                return;
            }
            case 1003: {
                Wat2WasmModuleMachineFuncGroup_0.func_1003(n, n2, memory, instance);
                return;
            }
            case 1058: {
                Wat2WasmModuleMachineFuncGroup_0.func_1058(n, n2, memory, instance);
                return;
            }
            case 1094: {
                Wat2WasmModuleMachineFuncGroup_0.func_1094(n, n2, memory, instance);
                return;
            }
            case 1095: {
                Wat2WasmModuleMachineFuncGroup_0.func_1095(n, n2, memory, instance);
                return;
            }
            case 1096: {
                Wat2WasmModuleMachineFuncGroup_0.func_1096(n, n2, memory, instance);
                return;
            }
            case 1105: {
                Wat2WasmModuleMachineFuncGroup_0.func_1105(n, n2, memory, instance);
                return;
            }
            case 1108: {
                Wat2WasmModuleMachineFuncGroup_0.func_1108(n, n2, memory, instance);
                return;
            }
            case 1153: {
                Wat2WasmModuleMachineFuncGroup_0.func_1153(n, n2, memory, instance);
                return;
            }
            case 1162: {
                Wat2WasmModuleMachineFuncGroup_0.func_1162(n, n2, memory, instance);
                return;
            }
            case 1185: {
                Wat2WasmModuleMachineFuncGroup_0.func_1185(n, n2, memory, instance);
                return;
            }
            case 1188: {
                Wat2WasmModuleMachineFuncGroup_0.func_1188(n, n2, memory, instance);
                return;
            }
            case 1189: {
                Wat2WasmModuleMachineFuncGroup_0.func_1189(n, n2, memory, instance);
                return;
            }
            case 1210: {
                Wat2WasmModuleMachineFuncGroup_0.func_1210(n, n2, memory, instance);
                return;
            }
            case 1218: {
                Wat2WasmModuleMachineFuncGroup_0.func_1218(n, n2, memory, instance);
                return;
            }
            case 1231: {
                Wat2WasmModuleMachineFuncGroup_0.func_1231(n, n2, memory, instance);
                return;
            }
            case 1252: {
                Wat2WasmModuleMachineFuncGroup_0.func_1252(n, n2, memory, instance);
                return;
            }
            case 1283: {
                Wat2WasmModuleMachineFuncGroup_0.func_1283(n, n2, memory, instance);
                return;
            }
            case 1303: {
                Wat2WasmModuleMachineFuncGroup_0.func_1303(n, n2, memory, instance);
                return;
            }
            case 1306: {
                Wat2WasmModuleMachineFuncGroup_0.func_1306(n, n2, memory, instance);
                return;
            }
            case 1409: {
                Wat2WasmModuleMachineFuncGroup_0.func_1409(n, n2, memory, instance);
                return;
            }
            case 1413: {
                Wat2WasmModuleMachineFuncGroup_0.func_1413(n, n2, memory, instance);
                return;
            }
            case 1580: {
                Wat2WasmModuleMachineFuncGroup_0.func_1580(n, n2, memory, instance);
                return;
            }
            case 1584: {
                Wat2WasmModuleMachineFuncGroup_0.func_1584(n, n2, memory, instance);
                return;
            }
            case 1589: {
                Wat2WasmModuleMachineFuncGroup_0.func_1589(n, n2, memory, instance);
                return;
            }
            case 1608: {
                Wat2WasmModuleMachineFuncGroup_0.func_1608(n, n2, memory, instance);
                return;
            }
            case 1609: {
                Wat2WasmModuleMachineFuncGroup_0.func_1609(n, n2, memory, instance);
                return;
            }
            case 1610: {
                Wat2WasmModuleMachineFuncGroup_0.func_1610(n, n2, memory, instance);
                return;
            }
            case 1617: {
                Wat2WasmModuleMachineFuncGroup_0.func_1617(n, n2, memory, instance);
                return;
            }
            case 1621: {
                Wat2WasmModuleMachineFuncGroup_0.func_1621(n, n2, memory, instance);
                return;
            }
            case 1622: {
                Wat2WasmModuleMachineFuncGroup_0.func_1622(n, n2, memory, instance);
                return;
            }
            case 1623: {
                Wat2WasmModuleMachineFuncGroup_0.func_1623(n, n2, memory, instance);
                return;
            }
            case 1628: {
                Wat2WasmModuleMachineFuncGroup_0.func_1628(n, n2, memory, instance);
                return;
            }
            case 1660: {
                Wat2WasmModuleMachineFuncGroup_0.func_1660(n, n2, memory, instance);
                return;
            }
            case 1664: {
                Wat2WasmModuleMachineFuncGroup_0.func_1664(n, n2, memory, instance);
                return;
            }
            case 1665: {
                Wat2WasmModuleMachineFuncGroup_0.func_1665(n, n2, memory, instance);
                return;
            }
            case 1668: {
                Wat2WasmModuleMachineFuncGroup_0.func_1668(n, n2, memory, instance);
                return;
            }
            case 1669: {
                Wat2WasmModuleMachineFuncGroup_0.func_1669(n, n2, memory, instance);
                return;
            }
            case 1710: {
                Wat2WasmModuleMachineFuncGroup_0.func_1710(n, n2, memory, instance);
                return;
            }
            case 1711: {
                Wat2WasmModuleMachineFuncGroup_0.func_1711(n, n2, memory, instance);
                return;
            }
            case 1780: {
                Wat2WasmModuleMachineFuncGroup_0.func_1780(n, n2, memory, instance);
                return;
            }
            case 1787: {
                Wat2WasmModuleMachineFuncGroup_0.func_1787(n, n2, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_4(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n6);
        final int requiredRef = table.requiredRef(n5);
        final Instance instance2 = table.instance(n5);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4 }, 4, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 9: {
                return Wat2WasmModuleMachineFuncGroup_0.func_9(n, n2, n3, n4, memory, instance);
            }
            case 11: {
                return Wat2WasmModuleMachineFuncGroup_0.func_11(n, n2, n3, n4, memory, instance);
            }
            case 48: {
                return Wat2WasmModuleMachineFuncGroup_0.func_48(n, n2, n3, n4, memory, instance);
            }
            case 77: {
                return Wat2WasmModuleMachineFuncGroup_0.func_77(n, n2, n3, n4, memory, instance);
            }
            case 168: {
                return Wat2WasmModuleMachineFuncGroup_0.func_168(n, n2, n3, n4, memory, instance);
            }
            case 171: {
                return Wat2WasmModuleMachineFuncGroup_0.func_171(n, n2, n3, n4, memory, instance);
            }
            case 182: {
                return Wat2WasmModuleMachineFuncGroup_0.func_182(n, n2, n3, n4, memory, instance);
            }
            case 197: {
                return Wat2WasmModuleMachineFuncGroup_0.func_197(n, n2, n3, n4, memory, instance);
            }
            case 231: {
                return Wat2WasmModuleMachineFuncGroup_0.func_231(n, n2, n3, n4, memory, instance);
            }
            case 232: {
                return Wat2WasmModuleMachineFuncGroup_0.func_232(n, n2, n3, n4, memory, instance);
            }
            case 237: {
                return Wat2WasmModuleMachineFuncGroup_0.func_237(n, n2, n3, n4, memory, instance);
            }
            case 238: {
                return Wat2WasmModuleMachineFuncGroup_0.func_238(n, n2, n3, n4, memory, instance);
            }
            case 256: {
                return Wat2WasmModuleMachineFuncGroup_0.func_256(n, n2, n3, n4, memory, instance);
            }
            case 258: {
                return Wat2WasmModuleMachineFuncGroup_0.func_258(n, n2, n3, n4, memory, instance);
            }
            case 277: {
                return Wat2WasmModuleMachineFuncGroup_0.func_277(n, n2, n3, n4, memory, instance);
            }
            case 288: {
                return Wat2WasmModuleMachineFuncGroup_0.func_288(n, n2, n3, n4, memory, instance);
            }
            case 337: {
                return Wat2WasmModuleMachineFuncGroup_0.func_337(n, n2, n3, n4, memory, instance);
            }
            case 339: {
                return Wat2WasmModuleMachineFuncGroup_0.func_339(n, n2, n3, n4, memory, instance);
            }
            case 345: {
                return Wat2WasmModuleMachineFuncGroup_0.func_345(n, n2, n3, n4, memory, instance);
            }
            case 346: {
                return Wat2WasmModuleMachineFuncGroup_0.func_346(n, n2, n3, n4, memory, instance);
            }
            case 347: {
                return Wat2WasmModuleMachineFuncGroup_0.func_347(n, n2, n3, n4, memory, instance);
            }
            case 348: {
                return Wat2WasmModuleMachineFuncGroup_0.func_348(n, n2, n3, n4, memory, instance);
            }
            case 360: {
                return Wat2WasmModuleMachineFuncGroup_0.func_360(n, n2, n3, n4, memory, instance);
            }
            case 363: {
                return Wat2WasmModuleMachineFuncGroup_0.func_363(n, n2, n3, n4, memory, instance);
            }
            case 372: {
                return Wat2WasmModuleMachineFuncGroup_0.func_372(n, n2, n3, n4, memory, instance);
            }
            case 388: {
                return Wat2WasmModuleMachineFuncGroup_0.func_388(n, n2, n3, n4, memory, instance);
            }
            case 390: {
                return Wat2WasmModuleMachineFuncGroup_0.func_390(n, n2, n3, n4, memory, instance);
            }
            case 410: {
                return Wat2WasmModuleMachineFuncGroup_0.func_410(n, n2, n3, n4, memory, instance);
            }
            case 413: {
                return Wat2WasmModuleMachineFuncGroup_0.func_413(n, n2, n3, n4, memory, instance);
            }
            case 422: {
                return Wat2WasmModuleMachineFuncGroup_0.func_422(n, n2, n3, n4, memory, instance);
            }
            case 424: {
                return Wat2WasmModuleMachineFuncGroup_0.func_424(n, n2, n3, n4, memory, instance);
            }
            case 428: {
                return Wat2WasmModuleMachineFuncGroup_0.func_428(n, n2, n3, n4, memory, instance);
            }
            case 430: {
                return Wat2WasmModuleMachineFuncGroup_0.func_430(n, n2, n3, n4, memory, instance);
            }
            case 434: {
                return Wat2WasmModuleMachineFuncGroup_0.func_434(n, n2, n3, n4, memory, instance);
            }
            case 540: {
                return Wat2WasmModuleMachineFuncGroup_0.func_540(n, n2, n3, n4, memory, instance);
            }
            case 541: {
                return Wat2WasmModuleMachineFuncGroup_0.func_541(n, n2, n3, n4, memory, instance);
            }
            case 542: {
                return Wat2WasmModuleMachineFuncGroup_0.func_542(n, n2, n3, n4, memory, instance);
            }
            case 543: {
                return Wat2WasmModuleMachineFuncGroup_0.func_543(n, n2, n3, n4, memory, instance);
            }
            case 548: {
                return Wat2WasmModuleMachineFuncGroup_0.func_548(n, n2, n3, n4, memory, instance);
            }
            case 549: {
                return Wat2WasmModuleMachineFuncGroup_0.func_549(n, n2, n3, n4, memory, instance);
            }
            case 550: {
                return Wat2WasmModuleMachineFuncGroup_0.func_550(n, n2, n3, n4, memory, instance);
            }
            case 551: {
                return Wat2WasmModuleMachineFuncGroup_0.func_551(n, n2, n3, n4, memory, instance);
            }
            case 552: {
                return Wat2WasmModuleMachineFuncGroup_0.func_552(n, n2, n3, n4, memory, instance);
            }
            case 554: {
                return Wat2WasmModuleMachineFuncGroup_0.func_554(n, n2, n3, n4, memory, instance);
            }
            case 556: {
                return Wat2WasmModuleMachineFuncGroup_0.func_556(n, n2, n3, n4, memory, instance);
            }
            case 578: {
                return Wat2WasmModuleMachineFuncGroup_0.func_578(n, n2, n3, n4, memory, instance);
            }
            case 579: {
                return Wat2WasmModuleMachineFuncGroup_0.func_579(n, n2, n3, n4, memory, instance);
            }
            case 581: {
                return Wat2WasmModuleMachineFuncGroup_0.func_581(n, n2, n3, n4, memory, instance);
            }
            case 589: {
                return Wat2WasmModuleMachineFuncGroup_0.func_589(n, n2, n3, n4, memory, instance);
            }
            case 590: {
                return Wat2WasmModuleMachineFuncGroup_0.func_590(n, n2, n3, n4, memory, instance);
            }
            case 591: {
                return Wat2WasmModuleMachineFuncGroup_0.func_591(n, n2, n3, n4, memory, instance);
            }
            case 594: {
                return Wat2WasmModuleMachineFuncGroup_0.func_594(n, n2, n3, n4, memory, instance);
            }
            case 598: {
                return Wat2WasmModuleMachineFuncGroup_0.func_598(n, n2, n3, n4, memory, instance);
            }
            case 611: {
                return Wat2WasmModuleMachineFuncGroup_0.func_611(n, n2, n3, n4, memory, instance);
            }
            case 614: {
                return Wat2WasmModuleMachineFuncGroup_0.func_614(n, n2, n3, n4, memory, instance);
            }
            case 616: {
                return Wat2WasmModuleMachineFuncGroup_0.func_616(n, n2, n3, n4, memory, instance);
            }
            case 618: {
                return Wat2WasmModuleMachineFuncGroup_0.func_618(n, n2, n3, n4, memory, instance);
            }
            case 619: {
                return Wat2WasmModuleMachineFuncGroup_0.func_619(n, n2, n3, n4, memory, instance);
            }
            case 620: {
                return Wat2WasmModuleMachineFuncGroup_0.func_620(n, n2, n3, n4, memory, instance);
            }
            case 621: {
                return Wat2WasmModuleMachineFuncGroup_0.func_621(n, n2, n3, n4, memory, instance);
            }
            case 625: {
                return Wat2WasmModuleMachineFuncGroup_0.func_625(n, n2, n3, n4, memory, instance);
            }
            case 632: {
                return Wat2WasmModuleMachineFuncGroup_0.func_632(n, n2, n3, n4, memory, instance);
            }
            case 637: {
                return Wat2WasmModuleMachineFuncGroup_0.func_637(n, n2, n3, n4, memory, instance);
            }
            case 641: {
                return Wat2WasmModuleMachineFuncGroup_0.func_641(n, n2, n3, n4, memory, instance);
            }
            case 746: {
                return Wat2WasmModuleMachineFuncGroup_0.func_746(n, n2, n3, n4, memory, instance);
            }
            case 791: {
                return Wat2WasmModuleMachineFuncGroup_0.func_791(n, n2, n3, n4, memory, instance);
            }
            case 801: {
                return Wat2WasmModuleMachineFuncGroup_0.func_801(n, n2, n3, n4, memory, instance);
            }
            case 847: {
                return Wat2WasmModuleMachineFuncGroup_0.func_847(n, n2, n3, n4, memory, instance);
            }
            case 848: {
                return Wat2WasmModuleMachineFuncGroup_0.func_848(n, n2, n3, n4, memory, instance);
            }
            case 854: {
                return Wat2WasmModuleMachineFuncGroup_0.func_854(n, n2, n3, n4, memory, instance);
            }
            case 874: {
                return Wat2WasmModuleMachineFuncGroup_0.func_874(n, n2, n3, n4, memory, instance);
            }
            case 878: {
                return Wat2WasmModuleMachineFuncGroup_0.func_878(n, n2, n3, n4, memory, instance);
            }
            case 882: {
                return Wat2WasmModuleMachineFuncGroup_0.func_882(n, n2, n3, n4, memory, instance);
            }
            case 884: {
                return Wat2WasmModuleMachineFuncGroup_0.func_884(n, n2, n3, n4, memory, instance);
            }
            case 899: {
                return Wat2WasmModuleMachineFuncGroup_0.func_899(n, n2, n3, n4, memory, instance);
            }
            case 908: {
                return Wat2WasmModuleMachineFuncGroup_0.func_908(n, n2, n3, n4, memory, instance);
            }
            case 928: {
                return Wat2WasmModuleMachineFuncGroup_0.func_928(n, n2, n3, n4, memory, instance);
            }
            case 998: {
                return Wat2WasmModuleMachineFuncGroup_0.func_998(n, n2, n3, n4, memory, instance);
            }
            case 1011: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1011(n, n2, n3, n4, memory, instance);
            }
            case 1021: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1021(n, n2, n3, n4, memory, instance);
            }
            case 1024: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1024(n, n2, n3, n4, memory, instance);
            }
            case 1028: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1028(n, n2, n3, n4, memory, instance);
            }
            case 1031: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1031(n, n2, n3, n4, memory, instance);
            }
            case 1033: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1033(n, n2, n3, n4, memory, instance);
            }
            case 1035: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1035(n, n2, n3, n4, memory, instance);
            }
            case 1047: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1047(n, n2, n3, n4, memory, instance);
            }
            case 1057: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1057(n, n2, n3, n4, memory, instance);
            }
            case 1065: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1065(n, n2, n3, n4, memory, instance);
            }
            case 1073: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1073(n, n2, n3, n4, memory, instance);
            }
            case 1216: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1216(n, n2, n3, n4, memory, instance);
            }
            case 1238: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1238(n, n2, n3, n4, memory, instance);
            }
            case 1272: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1272(n, n2, n3, n4, memory, instance);
            }
            case 1273: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1273(n, n2, n3, n4, memory, instance);
            }
            case 1281: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1281(n, n2, n3, n4, memory, instance);
            }
            case 1284: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1284(n, n2, n3, n4, memory, instance);
            }
            case 1288: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1288(n, n2, n3, n4, memory, instance);
            }
            case 1289: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1289(n, n2, n3, n4, memory, instance);
            }
            case 1290: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1290(n, n2, n3, n4, memory, instance);
            }
            case 1291: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1291(n, n2, n3, n4, memory, instance);
            }
            case 1292: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1292(n, n2, n3, n4, memory, instance);
            }
            case 1293: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1293(n, n2, n3, n4, memory, instance);
            }
            case 1295: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1295(n, n2, n3, n4, memory, instance);
            }
            case 1296: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1296(n, n2, n3, n4, memory, instance);
            }
            case 1301: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1301(n, n2, n3, n4, memory, instance);
            }
            case 1307: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1307(n, n2, n3, n4, memory, instance);
            }
            case 1648: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1648(n, n2, n3, n4, memory, instance);
            }
            case 1757: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1757(n, n2, n3, n4, memory, instance);
            }
            case 1801: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1801(n, n2, n3, n4, memory, instance);
            }
            case 1803: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1803(n, n2, n3, n4, memory, instance);
            }
            case 1804: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1804(n, n2, n3, n4, memory, instance);
            }
            case 1810: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1810(n, n2, n3, n4, memory, instance);
            }
            case 1815: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1815(n, n2, n3, n4, memory, instance);
            }
            case 1851: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1851(n, n2, n3, n4, memory, instance);
            }
            case 1856: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1856(n, n2, n3, n4, memory, instance);
            }
            case 1865: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1865(n, n2, n3, n4, memory, instance);
            }
            case 1875: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1875(n, n2, n3, n4, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_5(final int n, final int n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2 }, 5, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 0: {
                return Wat2WasmModuleMachineFuncGroup_0.func_0(n, n2, memory, instance);
            }
            case 1: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1(n, n2, memory, instance);
            }
            case 2: {
                return Wat2WasmModuleMachineFuncGroup_0.func_2(n, n2, memory, instance);
            }
            case 3: {
                return Wat2WasmModuleMachineFuncGroup_0.func_3(n, n2, memory, instance);
            }
            case 5: {
                return Wat2WasmModuleMachineFuncGroup_0.func_5(n, n2, memory, instance);
            }
            case 6: {
                return Wat2WasmModuleMachineFuncGroup_0.func_6(n, n2, memory, instance);
            }
            case 7: {
                return Wat2WasmModuleMachineFuncGroup_0.func_7(n, n2, memory, instance);
            }
            case 21: {
                return Wat2WasmModuleMachineFuncGroup_0.func_21(n, n2, memory, instance);
            }
            case 33: {
                return Wat2WasmModuleMachineFuncGroup_0.func_33(n, n2, memory, instance);
            }
            case 44: {
                return Wat2WasmModuleMachineFuncGroup_0.func_44(n, n2, memory, instance);
            }
            case 71: {
                return Wat2WasmModuleMachineFuncGroup_0.func_71(n, n2, memory, instance);
            }
            case 82: {
                return Wat2WasmModuleMachineFuncGroup_0.func_82(n, n2, memory, instance);
            }
            case 116: {
                return Wat2WasmModuleMachineFuncGroup_0.func_116(n, n2, memory, instance);
            }
            case 119: {
                return Wat2WasmModuleMachineFuncGroup_0.func_119(n, n2, memory, instance);
            }
            case 120: {
                return Wat2WasmModuleMachineFuncGroup_0.func_120(n, n2, memory, instance);
            }
            case 121: {
                return Wat2WasmModuleMachineFuncGroup_0.func_121(n, n2, memory, instance);
            }
            case 122: {
                return Wat2WasmModuleMachineFuncGroup_0.func_122(n, n2, memory, instance);
            }
            case 123: {
                return Wat2WasmModuleMachineFuncGroup_0.func_123(n, n2, memory, instance);
            }
            case 124: {
                return Wat2WasmModuleMachineFuncGroup_0.func_124(n, n2, memory, instance);
            }
            case 125: {
                return Wat2WasmModuleMachineFuncGroup_0.func_125(n, n2, memory, instance);
            }
            case 126: {
                return Wat2WasmModuleMachineFuncGroup_0.func_126(n, n2, memory, instance);
            }
            case 127: {
                return Wat2WasmModuleMachineFuncGroup_0.func_127(n, n2, memory, instance);
            }
            case 128: {
                return Wat2WasmModuleMachineFuncGroup_0.func_128(n, n2, memory, instance);
            }
            case 131: {
                return Wat2WasmModuleMachineFuncGroup_0.func_131(n, n2, memory, instance);
            }
            case 132: {
                return Wat2WasmModuleMachineFuncGroup_0.func_132(n, n2, memory, instance);
            }
            case 133: {
                return Wat2WasmModuleMachineFuncGroup_0.func_133(n, n2, memory, instance);
            }
            case 134: {
                return Wat2WasmModuleMachineFuncGroup_0.func_134(n, n2, memory, instance);
            }
            case 135: {
                return Wat2WasmModuleMachineFuncGroup_0.func_135(n, n2, memory, instance);
            }
            case 138: {
                return Wat2WasmModuleMachineFuncGroup_0.func_138(n, n2, memory, instance);
            }
            case 157: {
                return Wat2WasmModuleMachineFuncGroup_0.func_157(n, n2, memory, instance);
            }
            case 158: {
                return Wat2WasmModuleMachineFuncGroup_0.func_158(n, n2, memory, instance);
            }
            case 159: {
                return Wat2WasmModuleMachineFuncGroup_0.func_159(n, n2, memory, instance);
            }
            case 160: {
                return Wat2WasmModuleMachineFuncGroup_0.func_160(n, n2, memory, instance);
            }
            case 163: {
                return Wat2WasmModuleMachineFuncGroup_0.func_163(n, n2, memory, instance);
            }
            case 164: {
                return Wat2WasmModuleMachineFuncGroup_0.func_164(n, n2, memory, instance);
            }
            case 181: {
                return Wat2WasmModuleMachineFuncGroup_0.func_181(n, n2, memory, instance);
            }
            case 187: {
                return Wat2WasmModuleMachineFuncGroup_0.func_187(n, n2, memory, instance);
            }
            case 189: {
                return Wat2WasmModuleMachineFuncGroup_0.func_189(n, n2, memory, instance);
            }
            case 194: {
                return Wat2WasmModuleMachineFuncGroup_0.func_194(n, n2, memory, instance);
            }
            case 227: {
                return Wat2WasmModuleMachineFuncGroup_0.func_227(n, n2, memory, instance);
            }
            case 229: {
                return Wat2WasmModuleMachineFuncGroup_0.func_229(n, n2, memory, instance);
            }
            case 233: {
                return Wat2WasmModuleMachineFuncGroup_0.func_233(n, n2, memory, instance);
            }
            case 239: {
                return Wat2WasmModuleMachineFuncGroup_0.func_239(n, n2, memory, instance);
            }
            case 250: {
                return Wat2WasmModuleMachineFuncGroup_0.func_250(n, n2, memory, instance);
            }
            case 251: {
                return Wat2WasmModuleMachineFuncGroup_0.func_251(n, n2, memory, instance);
            }
            case 261: {
                return Wat2WasmModuleMachineFuncGroup_0.func_261(n, n2, memory, instance);
            }
            case 267: {
                return Wat2WasmModuleMachineFuncGroup_0.func_267(n, n2, memory, instance);
            }
            case 269: {
                return Wat2WasmModuleMachineFuncGroup_0.func_269(n, n2, memory, instance);
            }
            case 271: {
                return Wat2WasmModuleMachineFuncGroup_0.func_271(n, n2, memory, instance);
            }
            case 272: {
                return Wat2WasmModuleMachineFuncGroup_0.func_272(n, n2, memory, instance);
            }
            case 274: {
                return Wat2WasmModuleMachineFuncGroup_0.func_274(n, n2, memory, instance);
            }
            case 278: {
                return Wat2WasmModuleMachineFuncGroup_0.func_278(n, n2, memory, instance);
            }
            case 281: {
                return Wat2WasmModuleMachineFuncGroup_0.func_281(n, n2, memory, instance);
            }
            case 282: {
                return Wat2WasmModuleMachineFuncGroup_0.func_282(n, n2, memory, instance);
            }
            case 283: {
                return Wat2WasmModuleMachineFuncGroup_0.func_283(n, n2, memory, instance);
            }
            case 284: {
                return Wat2WasmModuleMachineFuncGroup_0.func_284(n, n2, memory, instance);
            }
            case 285: {
                return Wat2WasmModuleMachineFuncGroup_0.func_285(n, n2, memory, instance);
            }
            case 291: {
                return Wat2WasmModuleMachineFuncGroup_0.func_291(n, n2, memory, instance);
            }
            case 292: {
                return Wat2WasmModuleMachineFuncGroup_0.func_292(n, n2, memory, instance);
            }
            case 294: {
                return Wat2WasmModuleMachineFuncGroup_0.func_294(n, n2, memory, instance);
            }
            case 295: {
                return Wat2WasmModuleMachineFuncGroup_0.func_295(n, n2, memory, instance);
            }
            case 296: {
                return Wat2WasmModuleMachineFuncGroup_0.func_296(n, n2, memory, instance);
            }
            case 299: {
                return Wat2WasmModuleMachineFuncGroup_0.func_299(n, n2, memory, instance);
            }
            case 300: {
                return Wat2WasmModuleMachineFuncGroup_0.func_300(n, n2, memory, instance);
            }
            case 301: {
                return Wat2WasmModuleMachineFuncGroup_0.func_301(n, n2, memory, instance);
            }
            case 303: {
                return Wat2WasmModuleMachineFuncGroup_0.func_303(n, n2, memory, instance);
            }
            case 305: {
                return Wat2WasmModuleMachineFuncGroup_0.func_305(n, n2, memory, instance);
            }
            case 310: {
                return Wat2WasmModuleMachineFuncGroup_0.func_310(n, n2, memory, instance);
            }
            case 313: {
                return Wat2WasmModuleMachineFuncGroup_0.func_313(n, n2, memory, instance);
            }
            case 315: {
                return Wat2WasmModuleMachineFuncGroup_0.func_315(n, n2, memory, instance);
            }
            case 316: {
                return Wat2WasmModuleMachineFuncGroup_0.func_316(n, n2, memory, instance);
            }
            case 319: {
                return Wat2WasmModuleMachineFuncGroup_0.func_319(n, n2, memory, instance);
            }
            case 322: {
                return Wat2WasmModuleMachineFuncGroup_0.func_322(n, n2, memory, instance);
            }
            case 325: {
                return Wat2WasmModuleMachineFuncGroup_0.func_325(n, n2, memory, instance);
            }
            case 326: {
                return Wat2WasmModuleMachineFuncGroup_0.func_326(n, n2, memory, instance);
            }
            case 333: {
                return Wat2WasmModuleMachineFuncGroup_0.func_333(n, n2, memory, instance);
            }
            case 353: {
                return Wat2WasmModuleMachineFuncGroup_0.func_353(n, n2, memory, instance);
            }
            case 371: {
                return Wat2WasmModuleMachineFuncGroup_0.func_371(n, n2, memory, instance);
            }
            case 384: {
                return Wat2WasmModuleMachineFuncGroup_0.func_384(n, n2, memory, instance);
            }
            case 386: {
                return Wat2WasmModuleMachineFuncGroup_0.func_386(n, n2, memory, instance);
            }
            case 396: {
                return Wat2WasmModuleMachineFuncGroup_0.func_396(n, n2, memory, instance);
            }
            case 398: {
                return Wat2WasmModuleMachineFuncGroup_0.func_398(n, n2, memory, instance);
            }
            case 399: {
                return Wat2WasmModuleMachineFuncGroup_0.func_399(n, n2, memory, instance);
            }
            case 415: {
                return Wat2WasmModuleMachineFuncGroup_0.func_415(n, n2, memory, instance);
            }
            case 418: {
                return Wat2WasmModuleMachineFuncGroup_0.func_418(n, n2, memory, instance);
            }
            case 423: {
                return Wat2WasmModuleMachineFuncGroup_0.func_423(n, n2, memory, instance);
            }
            case 439: {
                return Wat2WasmModuleMachineFuncGroup_0.func_439(n, n2, memory, instance);
            }
            case 445: {
                return Wat2WasmModuleMachineFuncGroup_0.func_445(n, n2, memory, instance);
            }
            case 446: {
                return Wat2WasmModuleMachineFuncGroup_0.func_446(n, n2, memory, instance);
            }
            case 447: {
                return Wat2WasmModuleMachineFuncGroup_0.func_447(n, n2, memory, instance);
            }
            case 448: {
                return Wat2WasmModuleMachineFuncGroup_0.func_448(n, n2, memory, instance);
            }
            case 450: {
                return Wat2WasmModuleMachineFuncGroup_0.func_450(n, n2, memory, instance);
            }
            case 453: {
                return Wat2WasmModuleMachineFuncGroup_0.func_453(n, n2, memory, instance);
            }
            case 454: {
                return Wat2WasmModuleMachineFuncGroup_0.func_454(n, n2, memory, instance);
            }
            case 461: {
                return Wat2WasmModuleMachineFuncGroup_0.func_461(n, n2, memory, instance);
            }
            case 462: {
                return Wat2WasmModuleMachineFuncGroup_0.func_462(n, n2, memory, instance);
            }
            case 463: {
                return Wat2WasmModuleMachineFuncGroup_0.func_463(n, n2, memory, instance);
            }
            case 464: {
                return Wat2WasmModuleMachineFuncGroup_0.func_464(n, n2, memory, instance);
            }
            case 465: {
                return Wat2WasmModuleMachineFuncGroup_0.func_465(n, n2, memory, instance);
            }
            case 466: {
                return Wat2WasmModuleMachineFuncGroup_0.func_466(n, n2, memory, instance);
            }
            case 467: {
                return Wat2WasmModuleMachineFuncGroup_0.func_467(n, n2, memory, instance);
            }
            case 468: {
                return Wat2WasmModuleMachineFuncGroup_0.func_468(n, n2, memory, instance);
            }
            case 469: {
                return Wat2WasmModuleMachineFuncGroup_0.func_469(n, n2, memory, instance);
            }
            case 470: {
                return Wat2WasmModuleMachineFuncGroup_0.func_470(n, n2, memory, instance);
            }
            case 471: {
                return Wat2WasmModuleMachineFuncGroup_0.func_471(n, n2, memory, instance);
            }
            case 472: {
                return Wat2WasmModuleMachineFuncGroup_0.func_472(n, n2, memory, instance);
            }
            case 473: {
                return Wat2WasmModuleMachineFuncGroup_0.func_473(n, n2, memory, instance);
            }
            case 474: {
                return Wat2WasmModuleMachineFuncGroup_0.func_474(n, n2, memory, instance);
            }
            case 475: {
                return Wat2WasmModuleMachineFuncGroup_0.func_475(n, n2, memory, instance);
            }
            case 476: {
                return Wat2WasmModuleMachineFuncGroup_0.func_476(n, n2, memory, instance);
            }
            case 477: {
                return Wat2WasmModuleMachineFuncGroup_0.func_477(n, n2, memory, instance);
            }
            case 478: {
                return Wat2WasmModuleMachineFuncGroup_0.func_478(n, n2, memory, instance);
            }
            case 479: {
                return Wat2WasmModuleMachineFuncGroup_0.func_479(n, n2, memory, instance);
            }
            case 480: {
                return Wat2WasmModuleMachineFuncGroup_0.func_480(n, n2, memory, instance);
            }
            case 481: {
                return Wat2WasmModuleMachineFuncGroup_0.func_481(n, n2, memory, instance);
            }
            case 482: {
                return Wat2WasmModuleMachineFuncGroup_0.func_482(n, n2, memory, instance);
            }
            case 483: {
                return Wat2WasmModuleMachineFuncGroup_0.func_483(n, n2, memory, instance);
            }
            case 484: {
                return Wat2WasmModuleMachineFuncGroup_0.func_484(n, n2, memory, instance);
            }
            case 485: {
                return Wat2WasmModuleMachineFuncGroup_0.func_485(n, n2, memory, instance);
            }
            case 486: {
                return Wat2WasmModuleMachineFuncGroup_0.func_486(n, n2, memory, instance);
            }
            case 487: {
                return Wat2WasmModuleMachineFuncGroup_0.func_487(n, n2, memory, instance);
            }
            case 488: {
                return Wat2WasmModuleMachineFuncGroup_0.func_488(n, n2, memory, instance);
            }
            case 489: {
                return Wat2WasmModuleMachineFuncGroup_0.func_489(n, n2, memory, instance);
            }
            case 490: {
                return Wat2WasmModuleMachineFuncGroup_0.func_490(n, n2, memory, instance);
            }
            case 491: {
                return Wat2WasmModuleMachineFuncGroup_0.func_491(n, n2, memory, instance);
            }
            case 492: {
                return Wat2WasmModuleMachineFuncGroup_0.func_492(n, n2, memory, instance);
            }
            case 493: {
                return Wat2WasmModuleMachineFuncGroup_0.func_493(n, n2, memory, instance);
            }
            case 494: {
                return Wat2WasmModuleMachineFuncGroup_0.func_494(n, n2, memory, instance);
            }
            case 495: {
                return Wat2WasmModuleMachineFuncGroup_0.func_495(n, n2, memory, instance);
            }
            case 496: {
                return Wat2WasmModuleMachineFuncGroup_0.func_496(n, n2, memory, instance);
            }
            case 497: {
                return Wat2WasmModuleMachineFuncGroup_0.func_497(n, n2, memory, instance);
            }
            case 498: {
                return Wat2WasmModuleMachineFuncGroup_0.func_498(n, n2, memory, instance);
            }
            case 499: {
                return Wat2WasmModuleMachineFuncGroup_0.func_499(n, n2, memory, instance);
            }
            case 500: {
                return Wat2WasmModuleMachineFuncGroup_0.func_500(n, n2, memory, instance);
            }
            case 501: {
                return Wat2WasmModuleMachineFuncGroup_0.func_501(n, n2, memory, instance);
            }
            case 502: {
                return Wat2WasmModuleMachineFuncGroup_0.func_502(n, n2, memory, instance);
            }
            case 503: {
                return Wat2WasmModuleMachineFuncGroup_0.func_503(n, n2, memory, instance);
            }
            case 504: {
                return Wat2WasmModuleMachineFuncGroup_0.func_504(n, n2, memory, instance);
            }
            case 505: {
                return Wat2WasmModuleMachineFuncGroup_0.func_505(n, n2, memory, instance);
            }
            case 506: {
                return Wat2WasmModuleMachineFuncGroup_0.func_506(n, n2, memory, instance);
            }
            case 507: {
                return Wat2WasmModuleMachineFuncGroup_0.func_507(n, n2, memory, instance);
            }
            case 508: {
                return Wat2WasmModuleMachineFuncGroup_0.func_508(n, n2, memory, instance);
            }
            case 509: {
                return Wat2WasmModuleMachineFuncGroup_0.func_509(n, n2, memory, instance);
            }
            case 510: {
                return Wat2WasmModuleMachineFuncGroup_0.func_510(n, n2, memory, instance);
            }
            case 511: {
                return Wat2WasmModuleMachineFuncGroup_0.func_511(n, n2, memory, instance);
            }
            case 512: {
                return Wat2WasmModuleMachineFuncGroup_0.func_512(n, n2, memory, instance);
            }
            case 513: {
                return Wat2WasmModuleMachineFuncGroup_0.func_513(n, n2, memory, instance);
            }
            case 515: {
                return Wat2WasmModuleMachineFuncGroup_0.func_515(n, n2, memory, instance);
            }
            case 516: {
                return Wat2WasmModuleMachineFuncGroup_0.func_516(n, n2, memory, instance);
            }
            case 517: {
                return Wat2WasmModuleMachineFuncGroup_0.func_517(n, n2, memory, instance);
            }
            case 518: {
                return Wat2WasmModuleMachineFuncGroup_0.func_518(n, n2, memory, instance);
            }
            case 519: {
                return Wat2WasmModuleMachineFuncGroup_0.func_519(n, n2, memory, instance);
            }
            case 520: {
                return Wat2WasmModuleMachineFuncGroup_0.func_520(n, n2, memory, instance);
            }
            case 521: {
                return Wat2WasmModuleMachineFuncGroup_0.func_521(n, n2, memory, instance);
            }
            case 522: {
                return Wat2WasmModuleMachineFuncGroup_0.func_522(n, n2, memory, instance);
            }
            case 523: {
                return Wat2WasmModuleMachineFuncGroup_0.func_523(n, n2, memory, instance);
            }
            case 524: {
                return Wat2WasmModuleMachineFuncGroup_0.func_524(n, n2, memory, instance);
            }
            case 525: {
                return Wat2WasmModuleMachineFuncGroup_0.func_525(n, n2, memory, instance);
            }
            case 526: {
                return Wat2WasmModuleMachineFuncGroup_0.func_526(n, n2, memory, instance);
            }
            case 527: {
                return Wat2WasmModuleMachineFuncGroup_0.func_527(n, n2, memory, instance);
            }
            case 528: {
                return Wat2WasmModuleMachineFuncGroup_0.func_528(n, n2, memory, instance);
            }
            case 529: {
                return Wat2WasmModuleMachineFuncGroup_0.func_529(n, n2, memory, instance);
            }
            case 530: {
                return Wat2WasmModuleMachineFuncGroup_0.func_530(n, n2, memory, instance);
            }
            case 531: {
                return Wat2WasmModuleMachineFuncGroup_0.func_531(n, n2, memory, instance);
            }
            case 532: {
                return Wat2WasmModuleMachineFuncGroup_0.func_532(n, n2, memory, instance);
            }
            case 533: {
                return Wat2WasmModuleMachineFuncGroup_0.func_533(n, n2, memory, instance);
            }
            case 546: {
                return Wat2WasmModuleMachineFuncGroup_0.func_546(n, n2, memory, instance);
            }
            case 570: {
                return Wat2WasmModuleMachineFuncGroup_0.func_570(n, n2, memory, instance);
            }
            case 571: {
                return Wat2WasmModuleMachineFuncGroup_0.func_571(n, n2, memory, instance);
            }
            case 575: {
                return Wat2WasmModuleMachineFuncGroup_0.func_575(n, n2, memory, instance);
            }
            case 577: {
                return Wat2WasmModuleMachineFuncGroup_0.func_577(n, n2, memory, instance);
            }
            case 595: {
                return Wat2WasmModuleMachineFuncGroup_0.func_595(n, n2, memory, instance);
            }
            case 596: {
                return Wat2WasmModuleMachineFuncGroup_0.func_596(n, n2, memory, instance);
            }
            case 597: {
                return Wat2WasmModuleMachineFuncGroup_0.func_597(n, n2, memory, instance);
            }
            case 599: {
                return Wat2WasmModuleMachineFuncGroup_0.func_599(n, n2, memory, instance);
            }
            case 601: {
                return Wat2WasmModuleMachineFuncGroup_0.func_601(n, n2, memory, instance);
            }
            case 602: {
                return Wat2WasmModuleMachineFuncGroup_0.func_602(n, n2, memory, instance);
            }
            case 604: {
                return Wat2WasmModuleMachineFuncGroup_0.func_604(n, n2, memory, instance);
            }
            case 605: {
                return Wat2WasmModuleMachineFuncGroup_0.func_605(n, n2, memory, instance);
            }
            case 607: {
                return Wat2WasmModuleMachineFuncGroup_0.func_607(n, n2, memory, instance);
            }
            case 615: {
                return Wat2WasmModuleMachineFuncGroup_0.func_615(n, n2, memory, instance);
            }
            case 623: {
                return Wat2WasmModuleMachineFuncGroup_0.func_623(n, n2, memory, instance);
            }
            case 644: {
                return Wat2WasmModuleMachineFuncGroup_0.func_644(n, n2, memory, instance);
            }
            case 645: {
                return Wat2WasmModuleMachineFuncGroup_0.func_645(n, n2, memory, instance);
            }
            case 647: {
                return Wat2WasmModuleMachineFuncGroup_0.func_647(n, n2, memory, instance);
            }
            case 648: {
                return Wat2WasmModuleMachineFuncGroup_0.func_648(n, n2, memory, instance);
            }
            case 650: {
                return Wat2WasmModuleMachineFuncGroup_0.func_650(n, n2, memory, instance);
            }
            case 651: {
                return Wat2WasmModuleMachineFuncGroup_0.func_651(n, n2, memory, instance);
            }
            case 654: {
                return Wat2WasmModuleMachineFuncGroup_0.func_654(n, n2, memory, instance);
            }
            case 655: {
                return Wat2WasmModuleMachineFuncGroup_0.func_655(n, n2, memory, instance);
            }
            case 657: {
                return Wat2WasmModuleMachineFuncGroup_0.func_657(n, n2, memory, instance);
            }
            case 658: {
                return Wat2WasmModuleMachineFuncGroup_0.func_658(n, n2, memory, instance);
            }
            case 660: {
                return Wat2WasmModuleMachineFuncGroup_0.func_660(n, n2, memory, instance);
            }
            case 661: {
                return Wat2WasmModuleMachineFuncGroup_0.func_661(n, n2, memory, instance);
            }
            case 662: {
                return Wat2WasmModuleMachineFuncGroup_0.func_662(n, n2, memory, instance);
            }
            case 663: {
                return Wat2WasmModuleMachineFuncGroup_0.func_663(n, n2, memory, instance);
            }
            case 664: {
                return Wat2WasmModuleMachineFuncGroup_0.func_664(n, n2, memory, instance);
            }
            case 666: {
                return Wat2WasmModuleMachineFuncGroup_0.func_666(n, n2, memory, instance);
            }
            case 667: {
                return Wat2WasmModuleMachineFuncGroup_0.func_667(n, n2, memory, instance);
            }
            case 669: {
                return Wat2WasmModuleMachineFuncGroup_0.func_669(n, n2, memory, instance);
            }
            case 670: {
                return Wat2WasmModuleMachineFuncGroup_0.func_670(n, n2, memory, instance);
            }
            case 672: {
                return Wat2WasmModuleMachineFuncGroup_0.func_672(n, n2, memory, instance);
            }
            case 673: {
                return Wat2WasmModuleMachineFuncGroup_0.func_673(n, n2, memory, instance);
            }
            case 674: {
                return Wat2WasmModuleMachineFuncGroup_0.func_674(n, n2, memory, instance);
            }
            case 675: {
                return Wat2WasmModuleMachineFuncGroup_0.func_675(n, n2, memory, instance);
            }
            case 682: {
                return Wat2WasmModuleMachineFuncGroup_0.func_682(n, n2, memory, instance);
            }
            case 684: {
                return Wat2WasmModuleMachineFuncGroup_0.func_684(n, n2, memory, instance);
            }
            case 685: {
                return Wat2WasmModuleMachineFuncGroup_0.func_685(n, n2, memory, instance);
            }
            case 688: {
                return Wat2WasmModuleMachineFuncGroup_0.func_688(n, n2, memory, instance);
            }
            case 690: {
                return Wat2WasmModuleMachineFuncGroup_0.func_690(n, n2, memory, instance);
            }
            case 691: {
                return Wat2WasmModuleMachineFuncGroup_0.func_691(n, n2, memory, instance);
            }
            case 692: {
                return Wat2WasmModuleMachineFuncGroup_0.func_692(n, n2, memory, instance);
            }
            case 696: {
                return Wat2WasmModuleMachineFuncGroup_0.func_696(n, n2, memory, instance);
            }
            case 697: {
                return Wat2WasmModuleMachineFuncGroup_0.func_697(n, n2, memory, instance);
            }
            case 699: {
                return Wat2WasmModuleMachineFuncGroup_0.func_699(n, n2, memory, instance);
            }
            case 700: {
                return Wat2WasmModuleMachineFuncGroup_0.func_700(n, n2, memory, instance);
            }
            case 701: {
                return Wat2WasmModuleMachineFuncGroup_0.func_701(n, n2, memory, instance);
            }
            case 703: {
                return Wat2WasmModuleMachineFuncGroup_0.func_703(n, n2, memory, instance);
            }
            case 704: {
                return Wat2WasmModuleMachineFuncGroup_0.func_704(n, n2, memory, instance);
            }
            case 705: {
                return Wat2WasmModuleMachineFuncGroup_0.func_705(n, n2, memory, instance);
            }
            case 707: {
                return Wat2WasmModuleMachineFuncGroup_0.func_707(n, n2, memory, instance);
            }
            case 709: {
                return Wat2WasmModuleMachineFuncGroup_0.func_709(n, n2, memory, instance);
            }
            case 711: {
                return Wat2WasmModuleMachineFuncGroup_0.func_711(n, n2, memory, instance);
            }
            case 712: {
                return Wat2WasmModuleMachineFuncGroup_0.func_712(n, n2, memory, instance);
            }
            case 713: {
                return Wat2WasmModuleMachineFuncGroup_0.func_713(n, n2, memory, instance);
            }
            case 714: {
                return Wat2WasmModuleMachineFuncGroup_0.func_714(n, n2, memory, instance);
            }
            case 715: {
                return Wat2WasmModuleMachineFuncGroup_0.func_715(n, n2, memory, instance);
            }
            case 716: {
                return Wat2WasmModuleMachineFuncGroup_0.func_716(n, n2, memory, instance);
            }
            case 717: {
                return Wat2WasmModuleMachineFuncGroup_0.func_717(n, n2, memory, instance);
            }
            case 720: {
                return Wat2WasmModuleMachineFuncGroup_0.func_720(n, n2, memory, instance);
            }
            case 721: {
                return Wat2WasmModuleMachineFuncGroup_0.func_721(n, n2, memory, instance);
            }
            case 727: {
                return Wat2WasmModuleMachineFuncGroup_0.func_727(n, n2, memory, instance);
            }
            case 730: {
                return Wat2WasmModuleMachineFuncGroup_0.func_730(n, n2, memory, instance);
            }
            case 731: {
                return Wat2WasmModuleMachineFuncGroup_0.func_731(n, n2, memory, instance);
            }
            case 735: {
                return Wat2WasmModuleMachineFuncGroup_0.func_735(n, n2, memory, instance);
            }
            case 736: {
                return Wat2WasmModuleMachineFuncGroup_0.func_736(n, n2, memory, instance);
            }
            case 737: {
                return Wat2WasmModuleMachineFuncGroup_0.func_737(n, n2, memory, instance);
            }
            case 738: {
                return Wat2WasmModuleMachineFuncGroup_0.func_738(n, n2, memory, instance);
            }
            case 742: {
                return Wat2WasmModuleMachineFuncGroup_0.func_742(n, n2, memory, instance);
            }
            case 744: {
                return Wat2WasmModuleMachineFuncGroup_0.func_744(n, n2, memory, instance);
            }
            case 745: {
                return Wat2WasmModuleMachineFuncGroup_0.func_745(n, n2, memory, instance);
            }
            case 747: {
                return Wat2WasmModuleMachineFuncGroup_0.func_747(n, n2, memory, instance);
            }
            case 748: {
                return Wat2WasmModuleMachineFuncGroup_0.func_748(n, n2, memory, instance);
            }
            case 749: {
                return Wat2WasmModuleMachineFuncGroup_0.func_749(n, n2, memory, instance);
            }
            case 751: {
                return Wat2WasmModuleMachineFuncGroup_0.func_751(n, n2, memory, instance);
            }
            case 752: {
                return Wat2WasmModuleMachineFuncGroup_0.func_752(n, n2, memory, instance);
            }
            case 754: {
                return Wat2WasmModuleMachineFuncGroup_0.func_754(n, n2, memory, instance);
            }
            case 755: {
                return Wat2WasmModuleMachineFuncGroup_0.func_755(n, n2, memory, instance);
            }
            case 756: {
                return Wat2WasmModuleMachineFuncGroup_0.func_756(n, n2, memory, instance);
            }
            case 758: {
                return Wat2WasmModuleMachineFuncGroup_0.func_758(n, n2, memory, instance);
            }
            case 760: {
                return Wat2WasmModuleMachineFuncGroup_0.func_760(n, n2, memory, instance);
            }
            case 762: {
                return Wat2WasmModuleMachineFuncGroup_0.func_762(n, n2, memory, instance);
            }
            case 763: {
                return Wat2WasmModuleMachineFuncGroup_0.func_763(n, n2, memory, instance);
            }
            case 764: {
                return Wat2WasmModuleMachineFuncGroup_0.func_764(n, n2, memory, instance);
            }
            case 765: {
                return Wat2WasmModuleMachineFuncGroup_0.func_765(n, n2, memory, instance);
            }
            case 767: {
                return Wat2WasmModuleMachineFuncGroup_0.func_767(n, n2, memory, instance);
            }
            case 768: {
                return Wat2WasmModuleMachineFuncGroup_0.func_768(n, n2, memory, instance);
            }
            case 770: {
                return Wat2WasmModuleMachineFuncGroup_0.func_770(n, n2, memory, instance);
            }
            case 771: {
                return Wat2WasmModuleMachineFuncGroup_0.func_771(n, n2, memory, instance);
            }
            case 772: {
                return Wat2WasmModuleMachineFuncGroup_0.func_772(n, n2, memory, instance);
            }
            case 773: {
                return Wat2WasmModuleMachineFuncGroup_0.func_773(n, n2, memory, instance);
            }
            case 774: {
                return Wat2WasmModuleMachineFuncGroup_0.func_774(n, n2, memory, instance);
            }
            case 776: {
                return Wat2WasmModuleMachineFuncGroup_0.func_776(n, n2, memory, instance);
            }
            case 778: {
                return Wat2WasmModuleMachineFuncGroup_0.func_778(n, n2, memory, instance);
            }
            case 779: {
                return Wat2WasmModuleMachineFuncGroup_0.func_779(n, n2, memory, instance);
            }
            case 782: {
                return Wat2WasmModuleMachineFuncGroup_0.func_782(n, n2, memory, instance);
            }
            case 785: {
                return Wat2WasmModuleMachineFuncGroup_0.func_785(n, n2, memory, instance);
            }
            case 787: {
                return Wat2WasmModuleMachineFuncGroup_0.func_787(n, n2, memory, instance);
            }
            case 789: {
                return Wat2WasmModuleMachineFuncGroup_0.func_789(n, n2, memory, instance);
            }
            case 794: {
                return Wat2WasmModuleMachineFuncGroup_0.func_794(n, n2, memory, instance);
            }
            case 796: {
                return Wat2WasmModuleMachineFuncGroup_0.func_796(n, n2, memory, instance);
            }
            case 797: {
                return Wat2WasmModuleMachineFuncGroup_0.func_797(n, n2, memory, instance);
            }
            case 798: {
                return Wat2WasmModuleMachineFuncGroup_0.func_798(n, n2, memory, instance);
            }
            case 810: {
                return Wat2WasmModuleMachineFuncGroup_0.func_810(n, n2, memory, instance);
            }
            case 819: {
                return Wat2WasmModuleMachineFuncGroup_0.func_819(n, n2, memory, instance);
            }
            case 821: {
                return Wat2WasmModuleMachineFuncGroup_0.func_821(n, n2, memory, instance);
            }
            case 823: {
                return Wat2WasmModuleMachineFuncGroup_0.func_823(n, n2, memory, instance);
            }
            case 824: {
                return Wat2WasmModuleMachineFuncGroup_0.func_824(n, n2, memory, instance);
            }
            case 825: {
                return Wat2WasmModuleMachineFuncGroup_0.func_825(n, n2, memory, instance);
            }
            case 842: {
                return Wat2WasmModuleMachineFuncGroup_0.func_842(n, n2, memory, instance);
            }
            case 845: {
                return Wat2WasmModuleMachineFuncGroup_0.func_845(n, n2, memory, instance);
            }
            case 850: {
                return Wat2WasmModuleMachineFuncGroup_0.func_850(n, n2, memory, instance);
            }
            case 851: {
                return Wat2WasmModuleMachineFuncGroup_0.func_851(n, n2, memory, instance);
            }
            case 858: {
                return Wat2WasmModuleMachineFuncGroup_0.func_858(n, n2, memory, instance);
            }
            case 859: {
                return Wat2WasmModuleMachineFuncGroup_0.func_859(n, n2, memory, instance);
            }
            case 868: {
                return Wat2WasmModuleMachineFuncGroup_0.func_868(n, n2, memory, instance);
            }
            case 869: {
                return Wat2WasmModuleMachineFuncGroup_0.func_869(n, n2, memory, instance);
            }
            case 872: {
                return Wat2WasmModuleMachineFuncGroup_0.func_872(n, n2, memory, instance);
            }
            case 873: {
                return Wat2WasmModuleMachineFuncGroup_0.func_873(n, n2, memory, instance);
            }
            case 876: {
                return Wat2WasmModuleMachineFuncGroup_0.func_876(n, n2, memory, instance);
            }
            case 877: {
                return Wat2WasmModuleMachineFuncGroup_0.func_877(n, n2, memory, instance);
            }
            case 880: {
                return Wat2WasmModuleMachineFuncGroup_0.func_880(n, n2, memory, instance);
            }
            case 881: {
                return Wat2WasmModuleMachineFuncGroup_0.func_881(n, n2, memory, instance);
            }
            case 883: {
                return Wat2WasmModuleMachineFuncGroup_0.func_883(n, n2, memory, instance);
            }
            case 885: {
                return Wat2WasmModuleMachineFuncGroup_0.func_885(n, n2, memory, instance);
            }
            case 886: {
                return Wat2WasmModuleMachineFuncGroup_0.func_886(n, n2, memory, instance);
            }
            case 888: {
                return Wat2WasmModuleMachineFuncGroup_0.func_888(n, n2, memory, instance);
            }
            case 889: {
                return Wat2WasmModuleMachineFuncGroup_0.func_889(n, n2, memory, instance);
            }
            case 892: {
                return Wat2WasmModuleMachineFuncGroup_0.func_892(n, n2, memory, instance);
            }
            case 893: {
                return Wat2WasmModuleMachineFuncGroup_0.func_893(n, n2, memory, instance);
            }
            case 895: {
                return Wat2WasmModuleMachineFuncGroup_0.func_895(n, n2, memory, instance);
            }
            case 896: {
                return Wat2WasmModuleMachineFuncGroup_0.func_896(n, n2, memory, instance);
            }
            case 898: {
                return Wat2WasmModuleMachineFuncGroup_0.func_898(n, n2, memory, instance);
            }
            case 902: {
                return Wat2WasmModuleMachineFuncGroup_0.func_902(n, n2, memory, instance);
            }
            case 904: {
                return Wat2WasmModuleMachineFuncGroup_0.func_904(n, n2, memory, instance);
            }
            case 905: {
                return Wat2WasmModuleMachineFuncGroup_0.func_905(n, n2, memory, instance);
            }
            case 911: {
                return Wat2WasmModuleMachineFuncGroup_0.func_911(n, n2, memory, instance);
            }
            case 913: {
                return Wat2WasmModuleMachineFuncGroup_0.func_913(n, n2, memory, instance);
            }
            case 914: {
                return Wat2WasmModuleMachineFuncGroup_0.func_914(n, n2, memory, instance);
            }
            case 915: {
                return Wat2WasmModuleMachineFuncGroup_0.func_915(n, n2, memory, instance);
            }
            case 921: {
                return Wat2WasmModuleMachineFuncGroup_0.func_921(n, n2, memory, instance);
            }
            case 923: {
                return Wat2WasmModuleMachineFuncGroup_0.func_923(n, n2, memory, instance);
            }
            case 924: {
                return Wat2WasmModuleMachineFuncGroup_0.func_924(n, n2, memory, instance);
            }
            case 926: {
                return Wat2WasmModuleMachineFuncGroup_0.func_926(n, n2, memory, instance);
            }
            case 927: {
                return Wat2WasmModuleMachineFuncGroup_0.func_927(n, n2, memory, instance);
            }
            case 930: {
                return Wat2WasmModuleMachineFuncGroup_0.func_930(n, n2, memory, instance);
            }
            case 933: {
                return Wat2WasmModuleMachineFuncGroup_0.func_933(n, n2, memory, instance);
            }
            case 934: {
                return Wat2WasmModuleMachineFuncGroup_0.func_934(n, n2, memory, instance);
            }
            case 936: {
                return Wat2WasmModuleMachineFuncGroup_0.func_936(n, n2, memory, instance);
            }
            case 937: {
                return Wat2WasmModuleMachineFuncGroup_0.func_937(n, n2, memory, instance);
            }
            case 938: {
                return Wat2WasmModuleMachineFuncGroup_0.func_938(n, n2, memory, instance);
            }
            case 942: {
                return Wat2WasmModuleMachineFuncGroup_0.func_942(n, n2, memory, instance);
            }
            case 944: {
                return Wat2WasmModuleMachineFuncGroup_0.func_944(n, n2, memory, instance);
            }
            case 945: {
                return Wat2WasmModuleMachineFuncGroup_0.func_945(n, n2, memory, instance);
            }
            case 946: {
                return Wat2WasmModuleMachineFuncGroup_0.func_946(n, n2, memory, instance);
            }
            case 947: {
                return Wat2WasmModuleMachineFuncGroup_0.func_947(n, n2, memory, instance);
            }
            case 949: {
                return Wat2WasmModuleMachineFuncGroup_0.func_949(n, n2, memory, instance);
            }
            case 951: {
                return Wat2WasmModuleMachineFuncGroup_0.func_951(n, n2, memory, instance);
            }
            case 952: {
                return Wat2WasmModuleMachineFuncGroup_0.func_952(n, n2, memory, instance);
            }
            case 953: {
                return Wat2WasmModuleMachineFuncGroup_0.func_953(n, n2, memory, instance);
            }
            case 954: {
                return Wat2WasmModuleMachineFuncGroup_0.func_954(n, n2, memory, instance);
            }
            case 956: {
                return Wat2WasmModuleMachineFuncGroup_0.func_956(n, n2, memory, instance);
            }
            case 957: {
                return Wat2WasmModuleMachineFuncGroup_0.func_957(n, n2, memory, instance);
            }
            case 958: {
                return Wat2WasmModuleMachineFuncGroup_0.func_958(n, n2, memory, instance);
            }
            case 960: {
                return Wat2WasmModuleMachineFuncGroup_0.func_960(n, n2, memory, instance);
            }
            case 962: {
                return Wat2WasmModuleMachineFuncGroup_0.func_962(n, n2, memory, instance);
            }
            case 964: {
                return Wat2WasmModuleMachineFuncGroup_0.func_964(n, n2, memory, instance);
            }
            case 965: {
                return Wat2WasmModuleMachineFuncGroup_0.func_965(n, n2, memory, instance);
            }
            case 966: {
                return Wat2WasmModuleMachineFuncGroup_0.func_966(n, n2, memory, instance);
            }
            case 967: {
                return Wat2WasmModuleMachineFuncGroup_0.func_967(n, n2, memory, instance);
            }
            case 968: {
                return Wat2WasmModuleMachineFuncGroup_0.func_968(n, n2, memory, instance);
            }
            case 969: {
                return Wat2WasmModuleMachineFuncGroup_0.func_969(n, n2, memory, instance);
            }
            case 970: {
                return Wat2WasmModuleMachineFuncGroup_0.func_970(n, n2, memory, instance);
            }
            case 973: {
                return Wat2WasmModuleMachineFuncGroup_0.func_973(n, n2, memory, instance);
            }
            case 975: {
                return Wat2WasmModuleMachineFuncGroup_0.func_975(n, n2, memory, instance);
            }
            case 980: {
                return Wat2WasmModuleMachineFuncGroup_0.func_980(n, n2, memory, instance);
            }
            case 982: {
                return Wat2WasmModuleMachineFuncGroup_0.func_982(n, n2, memory, instance);
            }
            case 985: {
                return Wat2WasmModuleMachineFuncGroup_0.func_985(n, n2, memory, instance);
            }
            case 986: {
                return Wat2WasmModuleMachineFuncGroup_0.func_986(n, n2, memory, instance);
            }
            case 988: {
                return Wat2WasmModuleMachineFuncGroup_0.func_988(n, n2, memory, instance);
            }
            case 996: {
                return Wat2WasmModuleMachineFuncGroup_0.func_996(n, n2, memory, instance);
            }
            case 997: {
                return Wat2WasmModuleMachineFuncGroup_0.func_997(n, n2, memory, instance);
            }
            case 999: {
                return Wat2WasmModuleMachineFuncGroup_0.func_999(n, n2, memory, instance);
            }
            case 1000: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1000(n, n2, memory, instance);
            }
            case 1007: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1007(n, n2, memory, instance);
            }
            case 1009: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1009(n, n2, memory, instance);
            }
            case 1010: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1010(n, n2, memory, instance);
            }
            case 1012: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1012(n, n2, memory, instance);
            }
            case 1013: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1013(n, n2, memory, instance);
            }
            case 1015: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1015(n, n2, memory, instance);
            }
            case 1017: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1017(n, n2, memory, instance);
            }
            case 1018: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1018(n, n2, memory, instance);
            }
            case 1020: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1020(n, n2, memory, instance);
            }
            case 1022: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1022(n, n2, memory, instance);
            }
            case 1025: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1025(n, n2, memory, instance);
            }
            case 1029: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1029(n, n2, memory, instance);
            }
            case 1034: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1034(n, n2, memory, instance);
            }
            case 1039: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1039(n, n2, memory, instance);
            }
            case 1043: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1043(n, n2, memory, instance);
            }
            case 1045: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1045(n, n2, memory, instance);
            }
            case 1046: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1046(n, n2, memory, instance);
            }
            case 1049: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1049(n, n2, memory, instance);
            }
            case 1050: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1050(n, n2, memory, instance);
            }
            case 1052: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1052(n, n2, memory, instance);
            }
            case 1053: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1053(n, n2, memory, instance);
            }
            case 1056: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1056(n, n2, memory, instance);
            }
            case 1060: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1060(n, n2, memory, instance);
            }
            case 1061: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1061(n, n2, memory, instance);
            }
            case 1068: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1068(n, n2, memory, instance);
            }
            case 1070: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1070(n, n2, memory, instance);
            }
            case 1072: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1072(n, n2, memory, instance);
            }
            case 1076: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1076(n, n2, memory, instance);
            }
            case 1077: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1077(n, n2, memory, instance);
            }
            case 1081: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1081(n, n2, memory, instance);
            }
            case 1093: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1093(n, n2, memory, instance);
            }
            case 1097: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1097(n, n2, memory, instance);
            }
            case 1098: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1098(n, n2, memory, instance);
            }
            case 1104: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1104(n, n2, memory, instance);
            }
            case 1106: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1106(n, n2, memory, instance);
            }
            case 1107: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1107(n, n2, memory, instance);
            }
            case 1109: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1109(n, n2, memory, instance);
            }
            case 1110: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1110(n, n2, memory, instance);
            }
            case 1111: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1111(n, n2, memory, instance);
            }
            case 1112: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1112(n, n2, memory, instance);
            }
            case 1113: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1113(n, n2, memory, instance);
            }
            case 1114: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1114(n, n2, memory, instance);
            }
            case 1115: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1115(n, n2, memory, instance);
            }
            case 1116: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1116(n, n2, memory, instance);
            }
            case 1117: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1117(n, n2, memory, instance);
            }
            case 1118: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1118(n, n2, memory, instance);
            }
            case 1119: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1119(n, n2, memory, instance);
            }
            case 1120: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1120(n, n2, memory, instance);
            }
            case 1121: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1121(n, n2, memory, instance);
            }
            case 1122: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1122(n, n2, memory, instance);
            }
            case 1123: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1123(n, n2, memory, instance);
            }
            case 1124: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1124(n, n2, memory, instance);
            }
            case 1125: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1125(n, n2, memory, instance);
            }
            case 1126: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1126(n, n2, memory, instance);
            }
            case 1127: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1127(n, n2, memory, instance);
            }
            case 1128: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1128(n, n2, memory, instance);
            }
            case 1129: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1129(n, n2, memory, instance);
            }
            case 1130: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1130(n, n2, memory, instance);
            }
            case 1131: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1131(n, n2, memory, instance);
            }
            case 1132: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1132(n, n2, memory, instance);
            }
            case 1133: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1133(n, n2, memory, instance);
            }
            case 1134: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1134(n, n2, memory, instance);
            }
            case 1135: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1135(n, n2, memory, instance);
            }
            case 1136: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1136(n, n2, memory, instance);
            }
            case 1137: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1137(n, n2, memory, instance);
            }
            case 1138: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1138(n, n2, memory, instance);
            }
            case 1139: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1139(n, n2, memory, instance);
            }
            case 1140: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1140(n, n2, memory, instance);
            }
            case 1141: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1141(n, n2, memory, instance);
            }
            case 1142: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1142(n, n2, memory, instance);
            }
            case 1143: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1143(n, n2, memory, instance);
            }
            case 1145: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1145(n, n2, memory, instance);
            }
            case 1146: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1146(n, n2, memory, instance);
            }
            case 1147: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1147(n, n2, memory, instance);
            }
            case 1148: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1148(n, n2, memory, instance);
            }
            case 1149: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1149(n, n2, memory, instance);
            }
            case 1150: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1150(n, n2, memory, instance);
            }
            case 1154: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1154(n, n2, memory, instance);
            }
            case 1155: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1155(n, n2, memory, instance);
            }
            case 1158: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1158(n, n2, memory, instance);
            }
            case 1159: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1159(n, n2, memory, instance);
            }
            case 1160: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1160(n, n2, memory, instance);
            }
            case 1161: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1161(n, n2, memory, instance);
            }
            case 1165: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1165(n, n2, memory, instance);
            }
            case 1166: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1166(n, n2, memory, instance);
            }
            case 1167: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1167(n, n2, memory, instance);
            }
            case 1168: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1168(n, n2, memory, instance);
            }
            case 1171: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1171(n, n2, memory, instance);
            }
            case 1172: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1172(n, n2, memory, instance);
            }
            case 1173: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1173(n, n2, memory, instance);
            }
            case 1174: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1174(n, n2, memory, instance);
            }
            case 1175: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1175(n, n2, memory, instance);
            }
            case 1176: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1176(n, n2, memory, instance);
            }
            case 1177: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1177(n, n2, memory, instance);
            }
            case 1178: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1178(n, n2, memory, instance);
            }
            case 1179: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1179(n, n2, memory, instance);
            }
            case 1180: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1180(n, n2, memory, instance);
            }
            case 1181: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1181(n, n2, memory, instance);
            }
            case 1182: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1182(n, n2, memory, instance);
            }
            case 1183: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1183(n, n2, memory, instance);
            }
            case 1184: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1184(n, n2, memory, instance);
            }
            case 1186: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1186(n, n2, memory, instance);
            }
            case 1187: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1187(n, n2, memory, instance);
            }
            case 1190: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1190(n, n2, memory, instance);
            }
            case 1191: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1191(n, n2, memory, instance);
            }
            case 1194: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1194(n, n2, memory, instance);
            }
            case 1195: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1195(n, n2, memory, instance);
            }
            case 1197: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1197(n, n2, memory, instance);
            }
            case 1198: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1198(n, n2, memory, instance);
            }
            case 1200: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1200(n, n2, memory, instance);
            }
            case 1201: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1201(n, n2, memory, instance);
            }
            case 1203: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1203(n, n2, memory, instance);
            }
            case 1204: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1204(n, n2, memory, instance);
            }
            case 1206: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1206(n, n2, memory, instance);
            }
            case 1207: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1207(n, n2, memory, instance);
            }
            case 1209: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1209(n, n2, memory, instance);
            }
            case 1212: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1212(n, n2, memory, instance);
            }
            case 1213: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1213(n, n2, memory, instance);
            }
            case 1214: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1214(n, n2, memory, instance);
            }
            case 1219: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1219(n, n2, memory, instance);
            }
            case 1220: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1220(n, n2, memory, instance);
            }
            case 1221: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1221(n, n2, memory, instance);
            }
            case 1222: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1222(n, n2, memory, instance);
            }
            case 1223: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1223(n, n2, memory, instance);
            }
            case 1224: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1224(n, n2, memory, instance);
            }
            case 1225: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1225(n, n2, memory, instance);
            }
            case 1226: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1226(n, n2, memory, instance);
            }
            case 1227: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1227(n, n2, memory, instance);
            }
            case 1228: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1228(n, n2, memory, instance);
            }
            case 1229: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1229(n, n2, memory, instance);
            }
            case 1232: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1232(n, n2, memory, instance);
            }
            case 1233: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1233(n, n2, memory, instance);
            }
            case 1235: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1235(n, n2, memory, instance);
            }
            case 1240: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1240(n, n2, memory, instance);
            }
            case 1242: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1242(n, n2, memory, instance);
            }
            case 1243: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1243(n, n2, memory, instance);
            }
            case 1249: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1249(n, n2, memory, instance);
            }
            case 1254: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1254(n, n2, memory, instance);
            }
            case 1255: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1255(n, n2, memory, instance);
            }
            case 1257: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1257(n, n2, memory, instance);
            }
            case 1258: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1258(n, n2, memory, instance);
            }
            case 1297: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1297(n, n2, memory, instance);
            }
            case 1298: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1298(n, n2, memory, instance);
            }
            case 1318: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1318(n, n2, memory, instance);
            }
            case 1319: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1319(n, n2, memory, instance);
            }
            case 1321: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1321(n, n2, memory, instance);
            }
            case 1322: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1322(n, n2, memory, instance);
            }
            case 1323: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1323(n, n2, memory, instance);
            }
            case 1324: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1324(n, n2, memory, instance);
            }
            case 1325: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1325(n, n2, memory, instance);
            }
            case 1326: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1326(n, n2, memory, instance);
            }
            case 1327: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1327(n, n2, memory, instance);
            }
            case 1328: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1328(n, n2, memory, instance);
            }
            case 1329: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1329(n, n2, memory, instance);
            }
            case 1330: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1330(n, n2, memory, instance);
            }
            case 1331: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1331(n, n2, memory, instance);
            }
            case 1332: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1332(n, n2, memory, instance);
            }
            case 1333: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1333(n, n2, memory, instance);
            }
            case 1334: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1334(n, n2, memory, instance);
            }
            case 1335: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1335(n, n2, memory, instance);
            }
            case 1336: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1336(n, n2, memory, instance);
            }
            case 1337: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1337(n, n2, memory, instance);
            }
            case 1338: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1338(n, n2, memory, instance);
            }
            case 1339: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1339(n, n2, memory, instance);
            }
            case 1340: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1340(n, n2, memory, instance);
            }
            case 1341: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1341(n, n2, memory, instance);
            }
            case 1342: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1342(n, n2, memory, instance);
            }
            case 1343: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1343(n, n2, memory, instance);
            }
            case 1344: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1344(n, n2, memory, instance);
            }
            case 1345: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1345(n, n2, memory, instance);
            }
            case 1346: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1346(n, n2, memory, instance);
            }
            case 1347: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1347(n, n2, memory, instance);
            }
            case 1348: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1348(n, n2, memory, instance);
            }
            case 1349: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1349(n, n2, memory, instance);
            }
            case 1350: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1350(n, n2, memory, instance);
            }
            case 1351: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1351(n, n2, memory, instance);
            }
            case 1352: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1352(n, n2, memory, instance);
            }
            case 1353: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1353(n, n2, memory, instance);
            }
            case 1354: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1354(n, n2, memory, instance);
            }
            case 1355: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1355(n, n2, memory, instance);
            }
            case 1356: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1356(n, n2, memory, instance);
            }
            case 1357: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1357(n, n2, memory, instance);
            }
            case 1358: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1358(n, n2, memory, instance);
            }
            case 1359: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1359(n, n2, memory, instance);
            }
            case 1360: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1360(n, n2, memory, instance);
            }
            case 1361: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1361(n, n2, memory, instance);
            }
            case 1362: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1362(n, n2, memory, instance);
            }
            case 1363: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1363(n, n2, memory, instance);
            }
            case 1364: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1364(n, n2, memory, instance);
            }
            case 1365: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1365(n, n2, memory, instance);
            }
            case 1366: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1366(n, n2, memory, instance);
            }
            case 1367: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1367(n, n2, memory, instance);
            }
            case 1368: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1368(n, n2, memory, instance);
            }
            case 1369: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1369(n, n2, memory, instance);
            }
            case 1370: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1370(n, n2, memory, instance);
            }
            case 1371: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1371(n, n2, memory, instance);
            }
            case 1373: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1373(n, n2, memory, instance);
            }
            case 1374: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1374(n, n2, memory, instance);
            }
            case 1375: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1375(n, n2, memory, instance);
            }
            case 1376: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1376(n, n2, memory, instance);
            }
            case 1377: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1377(n, n2, memory, instance);
            }
            case 1378: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1378(n, n2, memory, instance);
            }
            case 1379: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1379(n, n2, memory, instance);
            }
            case 1380: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1380(n, n2, memory, instance);
            }
            case 1381: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1381(n, n2, memory, instance);
            }
            case 1382: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1382(n, n2, memory, instance);
            }
            case 1383: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1383(n, n2, memory, instance);
            }
            case 1384: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1384(n, n2, memory, instance);
            }
            case 1385: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1385(n, n2, memory, instance);
            }
            case 1386: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1386(n, n2, memory, instance);
            }
            case 1387: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1387(n, n2, memory, instance);
            }
            case 1388: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1388(n, n2, memory, instance);
            }
            case 1389: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1389(n, n2, memory, instance);
            }
            case 1390: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1390(n, n2, memory, instance);
            }
            case 1391: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1391(n, n2, memory, instance);
            }
            case 1407: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1407(n, n2, memory, instance);
            }
            case 1578: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1578(n, n2, memory, instance);
            }
            case 1581: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1581(n, n2, memory, instance);
            }
            case 1583: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1583(n, n2, memory, instance);
            }
            case 1586: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1586(n, n2, memory, instance);
            }
            case 1587: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1587(n, n2, memory, instance);
            }
            case 1592: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1592(n, n2, memory, instance);
            }
            case 1603: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1603(n, n2, memory, instance);
            }
            case 1613: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1613(n, n2, memory, instance);
            }
            case 1635: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1635(n, n2, memory, instance);
            }
            case 1650: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1650(n, n2, memory, instance);
            }
            case 1651: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1651(n, n2, memory, instance);
            }
            case 1657: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1657(n, n2, memory, instance);
            }
            case 1662: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1662(n, n2, memory, instance);
            }
            case 1680: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1680(n, n2, memory, instance);
            }
            case 1682: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1682(n, n2, memory, instance);
            }
            case 1692: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1692(n, n2, memory, instance);
            }
            case 1693: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1693(n, n2, memory, instance);
            }
            case 1699: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1699(n, n2, memory, instance);
            }
            case 1700: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1700(n, n2, memory, instance);
            }
            case 1706: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1706(n, n2, memory, instance);
            }
            case 1707: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1707(n, n2, memory, instance);
            }
            case 1724: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1724(n, n2, memory, instance);
            }
            case 1726: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1726(n, n2, memory, instance);
            }
            case 1727: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1727(n, n2, memory, instance);
            }
            case 1728: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1728(n, n2, memory, instance);
            }
            case 1729: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1729(n, n2, memory, instance);
            }
            case 1730: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1730(n, n2, memory, instance);
            }
            case 1731: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1731(n, n2, memory, instance);
            }
            case 1732: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1732(n, n2, memory, instance);
            }
            case 1733: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1733(n, n2, memory, instance);
            }
            case 1734: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1734(n, n2, memory, instance);
            }
            case 1735: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1735(n, n2, memory, instance);
            }
            case 1736: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1736(n, n2, memory, instance);
            }
            case 1738: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1738(n, n2, memory, instance);
            }
            case 1739: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1739(n, n2, memory, instance);
            }
            case 1754: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1754(n, n2, memory, instance);
            }
            case 1762: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1762(n, n2, memory, instance);
            }
            case 1774: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1774(n, n2, memory, instance);
            }
            case 1778: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1778(n, n2, memory, instance);
            }
            case 1779: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1779(n, n2, memory, instance);
            }
            case 1786: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1786(n, n2, memory, instance);
            }
            case 1788: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1788(n, n2, memory, instance);
            }
            case 1789: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1789(n, n2, memory, instance);
            }
            case 1792: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1792(n, n2, memory, instance);
            }
            case 1793: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1793(n, n2, memory, instance);
            }
            case 1794: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1794(n, n2, memory, instance);
            }
            case 1795: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1795(n, n2, memory, instance);
            }
            case 1797: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1797(n, n2, memory, instance);
            }
            case 1798: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1798(n, n2, memory, instance);
            }
            case 1799: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1799(n, n2, memory, instance);
            }
            case 1812: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1812(n, n2, memory, instance);
            }
            case 1813: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1813(n, n2, memory, instance);
            }
            case 1814: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1814(n, n2, memory, instance);
            }
            case 1828: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1828(n, n2, memory, instance);
            }
            case 1829: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1829(n, n2, memory, instance);
            }
            case 1845: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1845(n, n2, memory, instance);
            }
            case 1846: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1846(n, n2, memory, instance);
            }
            case 1848: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1848(n, n2, memory, instance);
            }
            case 1860: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1860(n, n2, memory, instance);
            }
            case 1861: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1861(n, n2, memory, instance);
            }
            case 1863: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1863(n, n2, memory, instance);
            }
            case 1868: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1868(n, n2, memory, instance);
            }
            case 1893: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1893(n, n2, memory, instance);
            }
            case 1894: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1894(n, n2, memory, instance);
            }
            case 1895: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1895(n, n2, memory, instance);
            }
            case 1899: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1899(n, n2, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_6(final int n, final int n2, final int n3, final int n4, final int n5, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n5);
        final int requiredRef = table.requiredRef(n4);
        final Instance instance2 = table.instance(n4);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3 }, 6, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 8: {
                return Wat2WasmModuleMachineFuncGroup_0.func_8(n, n2, n3, memory, instance);
            }
            case 40: {
                return Wat2WasmModuleMachineFuncGroup_0.func_40(n, n2, n3, memory, instance);
            }
            case 83: {
                return Wat2WasmModuleMachineFuncGroup_0.func_83(n, n2, n3, memory, instance);
            }
            case 86: {
                return Wat2WasmModuleMachineFuncGroup_0.func_86(n, n2, n3, memory, instance);
            }
            case 155: {
                return Wat2WasmModuleMachineFuncGroup_0.func_155(n, n2, n3, memory, instance);
            }
            case 156: {
                return Wat2WasmModuleMachineFuncGroup_0.func_156(n, n2, n3, memory, instance);
            }
            case 165: {
                return Wat2WasmModuleMachineFuncGroup_0.func_165(n, n2, n3, memory, instance);
            }
            case 172: {
                return Wat2WasmModuleMachineFuncGroup_0.func_172(n, n2, n3, memory, instance);
            }
            case 177: {
                return Wat2WasmModuleMachineFuncGroup_0.func_177(n, n2, n3, memory, instance);
            }
            case 178: {
                return Wat2WasmModuleMachineFuncGroup_0.func_178(n, n2, n3, memory, instance);
            }
            case 179: {
                return Wat2WasmModuleMachineFuncGroup_0.func_179(n, n2, n3, memory, instance);
            }
            case 180: {
                return Wat2WasmModuleMachineFuncGroup_0.func_180(n, n2, n3, memory, instance);
            }
            case 183: {
                return Wat2WasmModuleMachineFuncGroup_0.func_183(n, n2, n3, memory, instance);
            }
            case 184: {
                return Wat2WasmModuleMachineFuncGroup_0.func_184(n, n2, n3, memory, instance);
            }
            case 185: {
                return Wat2WasmModuleMachineFuncGroup_0.func_185(n, n2, n3, memory, instance);
            }
            case 188: {
                return Wat2WasmModuleMachineFuncGroup_0.func_188(n, n2, n3, memory, instance);
            }
            case 190: {
                return Wat2WasmModuleMachineFuncGroup_0.func_190(n, n2, n3, memory, instance);
            }
            case 198: {
                return Wat2WasmModuleMachineFuncGroup_0.func_198(n, n2, n3, memory, instance);
            }
            case 247: {
                return Wat2WasmModuleMachineFuncGroup_0.func_247(n, n2, n3, memory, instance);
            }
            case 255: {
                return Wat2WasmModuleMachineFuncGroup_0.func_255(n, n2, n3, memory, instance);
            }
            case 257: {
                return Wat2WasmModuleMachineFuncGroup_0.func_257(n, n2, n3, memory, instance);
            }
            case 262: {
                return Wat2WasmModuleMachineFuncGroup_0.func_262(n, n2, n3, memory, instance);
            }
            case 263: {
                return Wat2WasmModuleMachineFuncGroup_0.func_263(n, n2, n3, memory, instance);
            }
            case 264: {
                return Wat2WasmModuleMachineFuncGroup_0.func_264(n, n2, n3, memory, instance);
            }
            case 265: {
                return Wat2WasmModuleMachineFuncGroup_0.func_265(n, n2, n3, memory, instance);
            }
            case 266: {
                return Wat2WasmModuleMachineFuncGroup_0.func_266(n, n2, n3, memory, instance);
            }
            case 268: {
                return Wat2WasmModuleMachineFuncGroup_0.func_268(n, n2, n3, memory, instance);
            }
            case 270: {
                return Wat2WasmModuleMachineFuncGroup_0.func_270(n, n2, n3, memory, instance);
            }
            case 276: {
                return Wat2WasmModuleMachineFuncGroup_0.func_276(n, n2, n3, memory, instance);
            }
            case 279: {
                return Wat2WasmModuleMachineFuncGroup_0.func_279(n, n2, n3, memory, instance);
            }
            case 280: {
                return Wat2WasmModuleMachineFuncGroup_0.func_280(n, n2, n3, memory, instance);
            }
            case 290: {
                return Wat2WasmModuleMachineFuncGroup_0.func_290(n, n2, n3, memory, instance);
            }
            case 293: {
                return Wat2WasmModuleMachineFuncGroup_0.func_293(n, n2, n3, memory, instance);
            }
            case 297: {
                return Wat2WasmModuleMachineFuncGroup_0.func_297(n, n2, n3, memory, instance);
            }
            case 298: {
                return Wat2WasmModuleMachineFuncGroup_0.func_298(n, n2, n3, memory, instance);
            }
            case 302: {
                return Wat2WasmModuleMachineFuncGroup_0.func_302(n, n2, n3, memory, instance);
            }
            case 304: {
                return Wat2WasmModuleMachineFuncGroup_0.func_304(n, n2, n3, memory, instance);
            }
            case 306: {
                return Wat2WasmModuleMachineFuncGroup_0.func_306(n, n2, n3, memory, instance);
            }
            case 307: {
                return Wat2WasmModuleMachineFuncGroup_0.func_307(n, n2, n3, memory, instance);
            }
            case 308: {
                return Wat2WasmModuleMachineFuncGroup_0.func_308(n, n2, n3, memory, instance);
            }
            case 309: {
                return Wat2WasmModuleMachineFuncGroup_0.func_309(n, n2, n3, memory, instance);
            }
            case 311: {
                return Wat2WasmModuleMachineFuncGroup_0.func_311(n, n2, n3, memory, instance);
            }
            case 312: {
                return Wat2WasmModuleMachineFuncGroup_0.func_312(n, n2, n3, memory, instance);
            }
            case 320: {
                return Wat2WasmModuleMachineFuncGroup_0.func_320(n, n2, n3, memory, instance);
            }
            case 321: {
                return Wat2WasmModuleMachineFuncGroup_0.func_321(n, n2, n3, memory, instance);
            }
            case 323: {
                return Wat2WasmModuleMachineFuncGroup_0.func_323(n, n2, n3, memory, instance);
            }
            case 324: {
                return Wat2WasmModuleMachineFuncGroup_0.func_324(n, n2, n3, memory, instance);
            }
            case 330: {
                return Wat2WasmModuleMachineFuncGroup_0.func_330(n, n2, n3, memory, instance);
            }
            case 335: {
                return Wat2WasmModuleMachineFuncGroup_0.func_335(n, n2, n3, memory, instance);
            }
            case 340: {
                return Wat2WasmModuleMachineFuncGroup_0.func_340(n, n2, n3, memory, instance);
            }
            case 341: {
                return Wat2WasmModuleMachineFuncGroup_0.func_341(n, n2, n3, memory, instance);
            }
            case 342: {
                return Wat2WasmModuleMachineFuncGroup_0.func_342(n, n2, n3, memory, instance);
            }
            case 350: {
                return Wat2WasmModuleMachineFuncGroup_0.func_350(n, n2, n3, memory, instance);
            }
            case 354: {
                return Wat2WasmModuleMachineFuncGroup_0.func_354(n, n2, n3, memory, instance);
            }
            case 355: {
                return Wat2WasmModuleMachineFuncGroup_0.func_355(n, n2, n3, memory, instance);
            }
            case 357: {
                return Wat2WasmModuleMachineFuncGroup_0.func_357(n, n2, n3, memory, instance);
            }
            case 359: {
                return Wat2WasmModuleMachineFuncGroup_0.func_359(n, n2, n3, memory, instance);
            }
            case 361: {
                return Wat2WasmModuleMachineFuncGroup_0.func_361(n, n2, n3, memory, instance);
            }
            case 365: {
                return Wat2WasmModuleMachineFuncGroup_0.func_365(n, n2, n3, memory, instance);
            }
            case 368: {
                return Wat2WasmModuleMachineFuncGroup_0.func_368(n, n2, n3, memory, instance);
            }
            case 370: {
                return Wat2WasmModuleMachineFuncGroup_0.func_370(n, n2, n3, memory, instance);
            }
            case 373: {
                return Wat2WasmModuleMachineFuncGroup_0.func_373(n, n2, n3, memory, instance);
            }
            case 380: {
                return Wat2WasmModuleMachineFuncGroup_0.func_380(n, n2, n3, memory, instance);
            }
            case 381: {
                return Wat2WasmModuleMachineFuncGroup_0.func_381(n, n2, n3, memory, instance);
            }
            case 382: {
                return Wat2WasmModuleMachineFuncGroup_0.func_382(n, n2, n3, memory, instance);
            }
            case 383: {
                return Wat2WasmModuleMachineFuncGroup_0.func_383(n, n2, n3, memory, instance);
            }
            case 385: {
                return Wat2WasmModuleMachineFuncGroup_0.func_385(n, n2, n3, memory, instance);
            }
            case 387: {
                return Wat2WasmModuleMachineFuncGroup_0.func_387(n, n2, n3, memory, instance);
            }
            case 389: {
                return Wat2WasmModuleMachineFuncGroup_0.func_389(n, n2, n3, memory, instance);
            }
            case 391: {
                return Wat2WasmModuleMachineFuncGroup_0.func_391(n, n2, n3, memory, instance);
            }
            case 392: {
                return Wat2WasmModuleMachineFuncGroup_0.func_392(n, n2, n3, memory, instance);
            }
            case 393: {
                return Wat2WasmModuleMachineFuncGroup_0.func_393(n, n2, n3, memory, instance);
            }
            case 394: {
                return Wat2WasmModuleMachineFuncGroup_0.func_394(n, n2, n3, memory, instance);
            }
            case 395: {
                return Wat2WasmModuleMachineFuncGroup_0.func_395(n, n2, n3, memory, instance);
            }
            case 397: {
                return Wat2WasmModuleMachineFuncGroup_0.func_397(n, n2, n3, memory, instance);
            }
            case 400: {
                return Wat2WasmModuleMachineFuncGroup_0.func_400(n, n2, n3, memory, instance);
            }
            case 401: {
                return Wat2WasmModuleMachineFuncGroup_0.func_401(n, n2, n3, memory, instance);
            }
            case 402: {
                return Wat2WasmModuleMachineFuncGroup_0.func_402(n, n2, n3, memory, instance);
            }
            case 406: {
                return Wat2WasmModuleMachineFuncGroup_0.func_406(n, n2, n3, memory, instance);
            }
            case 407: {
                return Wat2WasmModuleMachineFuncGroup_0.func_407(n, n2, n3, memory, instance);
            }
            case 408: {
                return Wat2WasmModuleMachineFuncGroup_0.func_408(n, n2, n3, memory, instance);
            }
            case 409: {
                return Wat2WasmModuleMachineFuncGroup_0.func_409(n, n2, n3, memory, instance);
            }
            case 411: {
                return Wat2WasmModuleMachineFuncGroup_0.func_411(n, n2, n3, memory, instance);
            }
            case 412: {
                return Wat2WasmModuleMachineFuncGroup_0.func_412(n, n2, n3, memory, instance);
            }
            case 414: {
                return Wat2WasmModuleMachineFuncGroup_0.func_414(n, n2, n3, memory, instance);
            }
            case 416: {
                return Wat2WasmModuleMachineFuncGroup_0.func_416(n, n2, n3, memory, instance);
            }
            case 419: {
                return Wat2WasmModuleMachineFuncGroup_0.func_419(n, n2, n3, memory, instance);
            }
            case 420: {
                return Wat2WasmModuleMachineFuncGroup_0.func_420(n, n2, n3, memory, instance);
            }
            case 421: {
                return Wat2WasmModuleMachineFuncGroup_0.func_421(n, n2, n3, memory, instance);
            }
            case 431: {
                return Wat2WasmModuleMachineFuncGroup_0.func_431(n, n2, n3, memory, instance);
            }
            case 432: {
                return Wat2WasmModuleMachineFuncGroup_0.func_432(n, n2, n3, memory, instance);
            }
            case 433: {
                return Wat2WasmModuleMachineFuncGroup_0.func_433(n, n2, n3, memory, instance);
            }
            case 435: {
                return Wat2WasmModuleMachineFuncGroup_0.func_435(n, n2, n3, memory, instance);
            }
            case 436: {
                return Wat2WasmModuleMachineFuncGroup_0.func_436(n, n2, n3, memory, instance);
            }
            case 437: {
                return Wat2WasmModuleMachineFuncGroup_0.func_437(n, n2, n3, memory, instance);
            }
            case 438: {
                return Wat2WasmModuleMachineFuncGroup_0.func_438(n, n2, n3, memory, instance);
            }
            case 440: {
                return Wat2WasmModuleMachineFuncGroup_0.func_440(n, n2, n3, memory, instance);
            }
            case 441: {
                return Wat2WasmModuleMachineFuncGroup_0.func_441(n, n2, n3, memory, instance);
            }
            case 442: {
                return Wat2WasmModuleMachineFuncGroup_0.func_442(n, n2, n3, memory, instance);
            }
            case 443: {
                return Wat2WasmModuleMachineFuncGroup_0.func_443(n, n2, n3, memory, instance);
            }
            case 444: {
                return Wat2WasmModuleMachineFuncGroup_0.func_444(n, n2, n3, memory, instance);
            }
            case 457: {
                return Wat2WasmModuleMachineFuncGroup_0.func_457(n, n2, n3, memory, instance);
            }
            case 514: {
                return Wat2WasmModuleMachineFuncGroup_0.func_514(n, n2, n3, memory, instance);
            }
            case 539: {
                return Wat2WasmModuleMachineFuncGroup_0.func_539(n, n2, n3, memory, instance);
            }
            case 547: {
                return Wat2WasmModuleMachineFuncGroup_0.func_547(n, n2, n3, memory, instance);
            }
            case 553: {
                return Wat2WasmModuleMachineFuncGroup_0.func_553(n, n2, n3, memory, instance);
            }
            case 555: {
                return Wat2WasmModuleMachineFuncGroup_0.func_555(n, n2, n3, memory, instance);
            }
            case 572: {
                return Wat2WasmModuleMachineFuncGroup_0.func_572(n, n2, n3, memory, instance);
            }
            case 582: {
                return Wat2WasmModuleMachineFuncGroup_0.func_582(n, n2, n3, memory, instance);
            }
            case 593: {
                return Wat2WasmModuleMachineFuncGroup_0.func_593(n, n2, n3, memory, instance);
            }
            case 606: {
                return Wat2WasmModuleMachineFuncGroup_0.func_606(n, n2, n3, memory, instance);
            }
            case 608: {
                return Wat2WasmModuleMachineFuncGroup_0.func_608(n, n2, n3, memory, instance);
            }
            case 610: {
                return Wat2WasmModuleMachineFuncGroup_0.func_610(n, n2, n3, memory, instance);
            }
            case 612: {
                return Wat2WasmModuleMachineFuncGroup_0.func_612(n, n2, n3, memory, instance);
            }
            case 617: {
                return Wat2WasmModuleMachineFuncGroup_0.func_617(n, n2, n3, memory, instance);
            }
            case 624: {
                return Wat2WasmModuleMachineFuncGroup_0.func_624(n, n2, n3, memory, instance);
            }
            case 626: {
                return Wat2WasmModuleMachineFuncGroup_0.func_626(n, n2, n3, memory, instance);
            }
            case 628: {
                return Wat2WasmModuleMachineFuncGroup_0.func_628(n, n2, n3, memory, instance);
            }
            case 636: {
                return Wat2WasmModuleMachineFuncGroup_0.func_636(n, n2, n3, memory, instance);
            }
            case 638: {
                return Wat2WasmModuleMachineFuncGroup_0.func_638(n, n2, n3, memory, instance);
            }
            case 639: {
                return Wat2WasmModuleMachineFuncGroup_0.func_639(n, n2, n3, memory, instance);
            }
            case 652: {
                return Wat2WasmModuleMachineFuncGroup_0.func_652(n, n2, n3, memory, instance);
            }
            case 686: {
                return Wat2WasmModuleMachineFuncGroup_0.func_686(n, n2, n3, memory, instance);
            }
            case 702: {
                return Wat2WasmModuleMachineFuncGroup_0.func_702(n, n2, n3, memory, instance);
            }
            case 706: {
                return Wat2WasmModuleMachineFuncGroup_0.func_706(n, n2, n3, memory, instance);
            }
            case 708: {
                return Wat2WasmModuleMachineFuncGroup_0.func_708(n, n2, n3, memory, instance);
            }
            case 710: {
                return Wat2WasmModuleMachineFuncGroup_0.func_710(n, n2, n3, memory, instance);
            }
            case 722: {
                return Wat2WasmModuleMachineFuncGroup_0.func_722(n, n2, n3, memory, instance);
            }
            case 739: {
                return Wat2WasmModuleMachineFuncGroup_0.func_739(n, n2, n3, memory, instance);
            }
            case 740: {
                return Wat2WasmModuleMachineFuncGroup_0.func_740(n, n2, n3, memory, instance);
            }
            case 741: {
                return Wat2WasmModuleMachineFuncGroup_0.func_741(n, n2, n3, memory, instance);
            }
            case 757: {
                return Wat2WasmModuleMachineFuncGroup_0.func_757(n, n2, n3, memory, instance);
            }
            case 780: {
                return Wat2WasmModuleMachineFuncGroup_0.func_780(n, n2, n3, memory, instance);
            }
            case 783: {
                return Wat2WasmModuleMachineFuncGroup_0.func_783(n, n2, n3, memory, instance);
            }
            case 788: {
                return Wat2WasmModuleMachineFuncGroup_0.func_788(n, n2, n3, memory, instance);
            }
            case 790: {
                return Wat2WasmModuleMachineFuncGroup_0.func_790(n, n2, n3, memory, instance);
            }
            case 803: {
                return Wat2WasmModuleMachineFuncGroup_0.func_803(n, n2, n3, memory, instance);
            }
            case 804: {
                return Wat2WasmModuleMachineFuncGroup_0.func_804(n, n2, n3, memory, instance);
            }
            case 805: {
                return Wat2WasmModuleMachineFuncGroup_0.func_805(n, n2, n3, memory, instance);
            }
            case 806: {
                return Wat2WasmModuleMachineFuncGroup_0.func_806(n, n2, n3, memory, instance);
            }
            case 807: {
                return Wat2WasmModuleMachineFuncGroup_0.func_807(n, n2, n3, memory, instance);
            }
            case 808: {
                return Wat2WasmModuleMachineFuncGroup_0.func_808(n, n2, n3, memory, instance);
            }
            case 811: {
                return Wat2WasmModuleMachineFuncGroup_0.func_811(n, n2, n3, memory, instance);
            }
            case 812: {
                return Wat2WasmModuleMachineFuncGroup_0.func_812(n, n2, n3, memory, instance);
            }
            case 813: {
                return Wat2WasmModuleMachineFuncGroup_0.func_813(n, n2, n3, memory, instance);
            }
            case 814: {
                return Wat2WasmModuleMachineFuncGroup_0.func_814(n, n2, n3, memory, instance);
            }
            case 815: {
                return Wat2WasmModuleMachineFuncGroup_0.func_815(n, n2, n3, memory, instance);
            }
            case 817: {
                return Wat2WasmModuleMachineFuncGroup_0.func_817(n, n2, n3, memory, instance);
            }
            case 818: {
                return Wat2WasmModuleMachineFuncGroup_0.func_818(n, n2, n3, memory, instance);
            }
            case 826: {
                return Wat2WasmModuleMachineFuncGroup_0.func_826(n, n2, n3, memory, instance);
            }
            case 828: {
                return Wat2WasmModuleMachineFuncGroup_0.func_828(n, n2, n3, memory, instance);
            }
            case 856: {
                return Wat2WasmModuleMachineFuncGroup_0.func_856(n, n2, n3, memory, instance);
            }
            case 870: {
                return Wat2WasmModuleMachineFuncGroup_0.func_870(n, n2, n3, memory, instance);
            }
            case 897: {
                return Wat2WasmModuleMachineFuncGroup_0.func_897(n, n2, n3, memory, instance);
            }
            case 906: {
                return Wat2WasmModuleMachineFuncGroup_0.func_906(n, n2, n3, memory, instance);
            }
            case 907: {
                return Wat2WasmModuleMachineFuncGroup_0.func_907(n, n2, n3, memory, instance);
            }
            case 931: {
                return Wat2WasmModuleMachineFuncGroup_0.func_931(n, n2, n3, memory, instance);
            }
            case 955: {
                return Wat2WasmModuleMachineFuncGroup_0.func_955(n, n2, n3, memory, instance);
            }
            case 959: {
                return Wat2WasmModuleMachineFuncGroup_0.func_959(n, n2, n3, memory, instance);
            }
            case 961: {
                return Wat2WasmModuleMachineFuncGroup_0.func_961(n, n2, n3, memory, instance);
            }
            case 963: {
                return Wat2WasmModuleMachineFuncGroup_0.func_963(n, n2, n3, memory, instance);
            }
            case 976: {
                return Wat2WasmModuleMachineFuncGroup_0.func_976(n, n2, n3, memory, instance);
            }
            case 977: {
                return Wat2WasmModuleMachineFuncGroup_0.func_977(n, n2, n3, memory, instance);
            }
            case 983: {
                return Wat2WasmModuleMachineFuncGroup_0.func_983(n, n2, n3, memory, instance);
            }
            case 991: {
                return Wat2WasmModuleMachineFuncGroup_0.func_991(n, n2, n3, memory, instance);
            }
            case 1001: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1001(n, n2, n3, memory, instance);
            }
            case 1002: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1002(n, n2, n3, memory, instance);
            }
            case 1004: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1004(n, n2, n3, memory, instance);
            }
            case 1006: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1006(n, n2, n3, memory, instance);
            }
            case 1026: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1026(n, n2, n3, memory, instance);
            }
            case 1030: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1030(n, n2, n3, memory, instance);
            }
            case 1032: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1032(n, n2, n3, memory, instance);
            }
            case 1036: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1036(n, n2, n3, memory, instance);
            }
            case 1037: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1037(n, n2, n3, memory, instance);
            }
            case 1040: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1040(n, n2, n3, memory, instance);
            }
            case 1048: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1048(n, n2, n3, memory, instance);
            }
            case 1054: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1054(n, n2, n3, memory, instance);
            }
            case 1071: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1071(n, n2, n3, memory, instance);
            }
            case 1074: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1074(n, n2, n3, memory, instance);
            }
            case 1078: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1078(n, n2, n3, memory, instance);
            }
            case 1080: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1080(n, n2, n3, memory, instance);
            }
            case 1082: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1082(n, n2, n3, memory, instance);
            }
            case 1144: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1144(n, n2, n3, memory, instance);
            }
            case 1163: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1163(n, n2, n3, memory, instance);
            }
            case 1164: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1164(n, n2, n3, memory, instance);
            }
            case 1170: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1170(n, n2, n3, memory, instance);
            }
            case 1192: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1192(n, n2, n3, memory, instance);
            }
            case 1196: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1196(n, n2, n3, memory, instance);
            }
            case 1199: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1199(n, n2, n3, memory, instance);
            }
            case 1202: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1202(n, n2, n3, memory, instance);
            }
            case 1205: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1205(n, n2, n3, memory, instance);
            }
            case 1230: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1230(n, n2, n3, memory, instance);
            }
            case 1236: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1236(n, n2, n3, memory, instance);
            }
            case 1259: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1259(n, n2, n3, memory, instance);
            }
            case 1260: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1260(n, n2, n3, memory, instance);
            }
            case 1261: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1261(n, n2, n3, memory, instance);
            }
            case 1263: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1263(n, n2, n3, memory, instance);
            }
            case 1265: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1265(n, n2, n3, memory, instance);
            }
            case 1267: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1267(n, n2, n3, memory, instance);
            }
            case 1268: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1268(n, n2, n3, memory, instance);
            }
            case 1269: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1269(n, n2, n3, memory, instance);
            }
            case 1270: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1270(n, n2, n3, memory, instance);
            }
            case 1271: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1271(n, n2, n3, memory, instance);
            }
            case 1274: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1274(n, n2, n3, memory, instance);
            }
            case 1275: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1275(n, n2, n3, memory, instance);
            }
            case 1276: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1276(n, n2, n3, memory, instance);
            }
            case 1277: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1277(n, n2, n3, memory, instance);
            }
            case 1278: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1278(n, n2, n3, memory, instance);
            }
            case 1279: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1279(n, n2, n3, memory, instance);
            }
            case 1280: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1280(n, n2, n3, memory, instance);
            }
            case 1282: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1282(n, n2, n3, memory, instance);
            }
            case 1285: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1285(n, n2, n3, memory, instance);
            }
            case 1286: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1286(n, n2, n3, memory, instance);
            }
            case 1287: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1287(n, n2, n3, memory, instance);
            }
            case 1294: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1294(n, n2, n3, memory, instance);
            }
            case 1300: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1300(n, n2, n3, memory, instance);
            }
            case 1302: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1302(n, n2, n3, memory, instance);
            }
            case 1372: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1372(n, n2, n3, memory, instance);
            }
            case 1408: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1408(n, n2, n3, memory, instance);
            }
            case 1412: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1412(n, n2, n3, memory, instance);
            }
            case 1573: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1573(n, n2, n3, memory, instance);
            }
            case 1575: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1575(n, n2, n3, memory, instance);
            }
            case 1582: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1582(n, n2, n3, memory, instance);
            }
            case 1585: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1585(n, n2, n3, memory, instance);
            }
            case 1596: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1596(n, n2, n3, memory, instance);
            }
            case 1597: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1597(n, n2, n3, memory, instance);
            }
            case 1607: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1607(n, n2, n3, memory, instance);
            }
            case 1619: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1619(n, n2, n3, memory, instance);
            }
            case 1638: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1638(n, n2, n3, memory, instance);
            }
            case 1644: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1644(n, n2, n3, memory, instance);
            }
            case 1645: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1645(n, n2, n3, memory, instance);
            }
            case 1649: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1649(n, n2, n3, memory, instance);
            }
            case 1652: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1652(n, n2, n3, memory, instance);
            }
            case 1654: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1654(n, n2, n3, memory, instance);
            }
            case 1658: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1658(n, n2, n3, memory, instance);
            }
            case 1659: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1659(n, n2, n3, memory, instance);
            }
            case 1661: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1661(n, n2, n3, memory, instance);
            }
            case 1663: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1663(n, n2, n3, memory, instance);
            }
            case 1667: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1667(n, n2, n3, memory, instance);
            }
            case 1671: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1671(n, n2, n3, memory, instance);
            }
            case 1685: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1685(n, n2, n3, memory, instance);
            }
            case 1698: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1698(n, n2, n3, memory, instance);
            }
            case 1702: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1702(n, n2, n3, memory, instance);
            }
            case 1705: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1705(n, n2, n3, memory, instance);
            }
            case 1714: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1714(n, n2, n3, memory, instance);
            }
            case 1737: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1737(n, n2, n3, memory, instance);
            }
            case 1752: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1752(n, n2, n3, memory, instance);
            }
            case 1753: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1753(n, n2, n3, memory, instance);
            }
            case 1756: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1756(n, n2, n3, memory, instance);
            }
            case 1800: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1800(n, n2, n3, memory, instance);
            }
            case 1809: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1809(n, n2, n3, memory, instance);
            }
            case 1816: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1816(n, n2, n3, memory, instance);
            }
            case 1825: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1825(n, n2, n3, memory, instance);
            }
            case 1836: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1836(n, n2, n3, memory, instance);
            }
            case 1839: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1839(n, n2, n3, memory, instance);
            }
            case 1840: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1840(n, n2, n3, memory, instance);
            }
            case 1841: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1841(n, n2, n3, memory, instance);
            }
            case 1842: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1842(n, n2, n3, memory, instance);
            }
            case 1843: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1843(n, n2, n3, memory, instance);
            }
            case 1847: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1847(n, n2, n3, memory, instance);
            }
            case 1852: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1852(n, n2, n3, memory, instance);
            }
            case 1855: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1855(n, n2, n3, memory, instance);
            }
            case 1866: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1866(n, n2, n3, memory, instance);
            }
            case 1867: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1867(n, n2, n3, memory, instance);
            }
            case 1870: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1870(n, n2, n3, memory, instance);
            }
            case 1876: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1876(n, n2, n3, memory, instance);
            }
            case 1888: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1888(n, n2, n3, memory, instance);
            }
            case 1889: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1889(n, n2, n3, memory, instance);
            }
            case 1890: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1890(n, n2, n3, memory, instance);
            }
            case 1891: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1891(n, n2, n3, memory, instance);
            }
            case 1892: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1892(n, n2, n3, memory, instance);
            }
            case 1898: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1898(n, n2, n3, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_7(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n8);
        final int requiredRef = table.requiredRef(n7);
        final Instance instance2 = table.instance(n7);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6 }, 7, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 366: {
                return Wat2WasmModuleMachineFuncGroup_0.func_366(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 580: {
                return Wat2WasmModuleMachineFuncGroup_0.func_580(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 584: {
                return Wat2WasmModuleMachineFuncGroup_0.func_584(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 588: {
                return Wat2WasmModuleMachineFuncGroup_0.func_588(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 838: {
                return Wat2WasmModuleMachineFuncGroup_0.func_838(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 852: {
                return Wat2WasmModuleMachineFuncGroup_0.func_852(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 861: {
                return Wat2WasmModuleMachineFuncGroup_0.func_861(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 866: {
                return Wat2WasmModuleMachineFuncGroup_0.func_866(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 1237: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1237(n, n2, n3, n4, n5, n6, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_8(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n7);
        final int requiredRef = table.requiredRef(n6);
        final Instance instance2 = table.instance(n6);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5 }, 8, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 12: {
                return Wat2WasmModuleMachineFuncGroup_0.func_12(n, n2, n3, n4, n5, memory, instance);
            }
            case 17: {
                return Wat2WasmModuleMachineFuncGroup_0.func_17(n, n2, n3, n4, n5, memory, instance);
            }
            case 81: {
                return Wat2WasmModuleMachineFuncGroup_0.func_81(n, n2, n3, n4, n5, memory, instance);
            }
            case 245: {
                return Wat2WasmModuleMachineFuncGroup_0.func_245(n, n2, n3, n4, n5, memory, instance);
            }
            case 259: {
                return Wat2WasmModuleMachineFuncGroup_0.func_259(n, n2, n3, n4, n5, memory, instance);
            }
            case 260: {
                return Wat2WasmModuleMachineFuncGroup_0.func_260(n, n2, n3, n4, n5, memory, instance);
            }
            case 349: {
                return Wat2WasmModuleMachineFuncGroup_0.func_349(n, n2, n3, n4, n5, memory, instance);
            }
            case 352: {
                return Wat2WasmModuleMachineFuncGroup_0.func_352(n, n2, n3, n4, n5, memory, instance);
            }
            case 356: {
                return Wat2WasmModuleMachineFuncGroup_0.func_356(n, n2, n3, n4, n5, memory, instance);
            }
            case 358: {
                return Wat2WasmModuleMachineFuncGroup_0.func_358(n, n2, n3, n4, n5, memory, instance);
            }
            case 583: {
                return Wat2WasmModuleMachineFuncGroup_0.func_583(n, n2, n3, n4, n5, memory, instance);
            }
            case 592: {
                return Wat2WasmModuleMachineFuncGroup_0.func_592(n, n2, n3, n4, n5, memory, instance);
            }
            case 622: {
                return Wat2WasmModuleMachineFuncGroup_0.func_622(n, n2, n3, n4, n5, memory, instance);
            }
            case 627: {
                return Wat2WasmModuleMachineFuncGroup_0.func_627(n, n2, n3, n4, n5, memory, instance);
            }
            case 630: {
                return Wat2WasmModuleMachineFuncGroup_0.func_630(n, n2, n3, n4, n5, memory, instance);
            }
            case 631: {
                return Wat2WasmModuleMachineFuncGroup_0.func_631(n, n2, n3, n4, n5, memory, instance);
            }
            case 633: {
                return Wat2WasmModuleMachineFuncGroup_0.func_633(n, n2, n3, n4, n5, memory, instance);
            }
            case 634: {
                return Wat2WasmModuleMachineFuncGroup_0.func_634(n, n2, n3, n4, n5, memory, instance);
            }
            case 792: {
                return Wat2WasmModuleMachineFuncGroup_0.func_792(n, n2, n3, n4, n5, memory, instance);
            }
            case 860: {
                return Wat2WasmModuleMachineFuncGroup_0.func_860(n, n2, n3, n4, n5, memory, instance);
            }
            case 890: {
                return Wat2WasmModuleMachineFuncGroup_0.func_890(n, n2, n3, n4, n5, memory, instance);
            }
            case 909: {
                return Wat2WasmModuleMachineFuncGroup_0.func_909(n, n2, n3, n4, n5, memory, instance);
            }
            case 1041: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1041(n, n2, n3, n4, n5, memory, instance);
            }
            case 1044: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1044(n, n2, n3, n4, n5, memory, instance);
            }
            case 1063: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1063(n, n2, n3, n4, n5, memory, instance);
            }
            case 1064: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1064(n, n2, n3, n4, n5, memory, instance);
            }
            case 1066: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1066(n, n2, n3, n4, n5, memory, instance);
            }
            case 1067: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1067(n, n2, n3, n4, n5, memory, instance);
            }
            case 1308: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1308(n, n2, n3, n4, n5, memory, instance);
            }
            case 1577: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1577(n, n2, n3, n4, n5, memory, instance);
            }
            case 1653: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1653(n, n2, n3, n4, n5, memory, instance);
            }
            case 1871: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1871(n, n2, n3, n4, n5, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_9(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n9);
        final int requiredRef = table.requiredRef(n8);
        final Instance instance2 = table.instance(n8);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6, n7 }, 9, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 338: {
                return Wat2WasmModuleMachineFuncGroup_0.func_338(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 585: {
                return Wat2WasmModuleMachineFuncGroup_0.func_585(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 586: {
                return Wat2WasmModuleMachineFuncGroup_0.func_586(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 587: {
                return Wat2WasmModuleMachineFuncGroup_0.func_587(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 629: {
                return Wat2WasmModuleMachineFuncGroup_0.func_629(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 863: {
                return Wat2WasmModuleMachineFuncGroup_0.func_863(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 864: {
                return Wat2WasmModuleMachineFuncGroup_0.func_864(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 865: {
                return Wat2WasmModuleMachineFuncGroup_0.func_865(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 1062: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1062(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_10(final int n, final long n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2 }, 10, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 600: {
                return Wat2WasmModuleMachineFuncGroup_0.func_600(n, n2, memory, instance);
            }
            case 603: {
                return Wat2WasmModuleMachineFuncGroup_0.func_603(n, n2, memory, instance);
            }
            case 793: {
                return Wat2WasmModuleMachineFuncGroup_0.func_793(n, n2, memory, instance);
            }
            case 795: {
                return Wat2WasmModuleMachineFuncGroup_0.func_795(n, n2, memory, instance);
            }
            case 910: {
                return Wat2WasmModuleMachineFuncGroup_0.func_910(n, n2, memory, instance);
            }
            case 912: {
                return Wat2WasmModuleMachineFuncGroup_0.func_912(n, n2, memory, instance);
            }
            case 943: {
                return Wat2WasmModuleMachineFuncGroup_0.func_943(n, n2, memory, instance);
            }
            case 948: {
                return Wat2WasmModuleMachineFuncGroup_0.func_948(n, n2, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_11(final int n, final int n2, final long n3, final int n4, final int n5, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n5);
        final int requiredRef = table.requiredRef(n4);
        final Instance instance2 = table.instance(n4);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3 }, 11, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 327: {
                return Wat2WasmModuleMachineFuncGroup_0.func_327(n, n2, n3, memory, instance);
            }
            case 609: {
                return Wat2WasmModuleMachineFuncGroup_0.func_609(n, n2, n3, memory, instance);
            }
            case 990: {
                return Wat2WasmModuleMachineFuncGroup_0.func_990(n, n2, n3, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_12(final int n, final int n2, final int n3, final long n4, final int n5, final int n6, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n6);
        final int requiredRef = table.requiredRef(n5);
        final Instance instance2 = table.instance(n5);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4 }, 12, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 328: {
                return Wat2WasmModuleMachineFuncGroup_0.func_328(n, n2, n3, n4, memory, instance);
            }
            case 329: {
                return Wat2WasmModuleMachineFuncGroup_0.func_329(n, n2, n3, n4, memory, instance);
            }
            case 425: {
                return Wat2WasmModuleMachineFuncGroup_0.func_425(n, n2, n3, n4, memory, instance);
            }
            case 613: {
                return Wat2WasmModuleMachineFuncGroup_0.func_613(n, n2, n3, n4, memory, instance);
            }
            case 640: {
                return Wat2WasmModuleMachineFuncGroup_0.func_640(n, n2, n3, n4, memory, instance);
            }
            case 1014: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1014(n, n2, n3, n4, memory, instance);
            }
            case 1083: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1083(n, n2, n3, n4, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_13(final int n, final int n2, final int n3, final long n4, final int n5, final int n6, final int n7, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n7);
        final int requiredRef = table.requiredRef(n6);
        final Instance instance2 = table.instance(n6);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5 }, 13, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 635: {
                return Wat2WasmModuleMachineFuncGroup_0.func_635(n, n2, n3, n4, n5, memory, instance);
            }
            case 1069: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1069(n, n2, n3, n4, n5, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_14(final int n, final int n2, final int n3, final long n4, final long n5, final int n6, final int n7, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n7);
        final int requiredRef = table.requiredRef(n6);
        final Instance instance2 = table.instance(n6);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5 }, 14, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 677: {
                return Wat2WasmModuleMachineFuncGroup_0.func_677(n, n2, n3, n4, n5, memory, instance);
            }
            case 678: {
                return Wat2WasmModuleMachineFuncGroup_0.func_678(n, n2, n3, n4, n5, memory, instance);
            }
            case 679: {
                return Wat2WasmModuleMachineFuncGroup_0.func_679(n, n2, n3, n4, n5, memory, instance);
            }
            case 680: {
                return Wat2WasmModuleMachineFuncGroup_0.func_680(n, n2, n3, n4, n5, memory, instance);
            }
            case 681: {
                return Wat2WasmModuleMachineFuncGroup_0.func_681(n, n2, n3, n4, n5, memory, instance);
            }
            case 683: {
                return Wat2WasmModuleMachineFuncGroup_0.func_683(n, n2, n3, n4, n5, memory, instance);
            }
            case 698: {
                return Wat2WasmModuleMachineFuncGroup_0.func_698(n, n2, n3, n4, n5, memory, instance);
            }
            case 724: {
                return Wat2WasmModuleMachineFuncGroup_0.func_724(n, n2, n3, n4, n5, memory, instance);
            }
            case 725: {
                return Wat2WasmModuleMachineFuncGroup_0.func_725(n, n2, n3, n4, n5, memory, instance);
            }
            case 726: {
                return Wat2WasmModuleMachineFuncGroup_0.func_726(n, n2, n3, n4, n5, memory, instance);
            }
            case 916: {
                return Wat2WasmModuleMachineFuncGroup_0.func_916(n, n2, n3, n4, n5, memory, instance);
            }
            case 917: {
                return Wat2WasmModuleMachineFuncGroup_0.func_917(n, n2, n3, n4, n5, memory, instance);
            }
            case 918: {
                return Wat2WasmModuleMachineFuncGroup_0.func_918(n, n2, n3, n4, n5, memory, instance);
            }
            case 919: {
                return Wat2WasmModuleMachineFuncGroup_0.func_919(n, n2, n3, n4, n5, memory, instance);
            }
            case 920: {
                return Wat2WasmModuleMachineFuncGroup_0.func_920(n, n2, n3, n4, n5, memory, instance);
            }
            case 922: {
                return Wat2WasmModuleMachineFuncGroup_0.func_922(n, n2, n3, n4, n5, memory, instance);
            }
            case 950: {
                return Wat2WasmModuleMachineFuncGroup_0.func_950(n, n2, n3, n4, n5, memory, instance);
            }
            case 979: {
                return Wat2WasmModuleMachineFuncGroup_0.func_979(n, n2, n3, n4, n5, memory, instance);
            }
            case 994: {
                return Wat2WasmModuleMachineFuncGroup_0.func_994(n, n2, n3, n4, n5, memory, instance);
            }
            case 995: {
                return Wat2WasmModuleMachineFuncGroup_0.func_995(n, n2, n3, n4, n5, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_15(final int n, final int n2, final int n3, final long n4, final long n5, final long n6, final int n7, final int n8, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n8);
        final int requiredRef = table.requiredRef(n7);
        final Instance instance2 = table.instance(n7);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6 }, 15, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 732: {
                return Wat2WasmModuleMachineFuncGroup_0.func_732(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 733: {
                return Wat2WasmModuleMachineFuncGroup_0.func_733(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 992: {
                return Wat2WasmModuleMachineFuncGroup_0.func_992(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 993: {
                return Wat2WasmModuleMachineFuncGroup_0.func_993(n, n2, n3, n4, n5, n6, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_16(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n6);
        final int requiredRef = table.requiredRef(n5);
        final Instance instance2 = table.instance(n5);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4 }, 16, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 54: {
                Wat2WasmModuleMachineFuncGroup_0.func_54(n, n2, n3, n4, memory, instance);
                return;
            }
            case 63: {
                Wat2WasmModuleMachineFuncGroup_0.func_63(n, n2, n3, n4, memory, instance);
                return;
            }
            case 73: {
                Wat2WasmModuleMachineFuncGroup_0.func_73(n, n2, n3, n4, memory, instance);
                return;
            }
            case 114: {
                Wat2WasmModuleMachineFuncGroup_0.func_114(n, n2, n3, n4, memory, instance);
                return;
            }
            case 137: {
                Wat2WasmModuleMachineFuncGroup_0.func_137(n, n2, n3, n4, memory, instance);
                return;
            }
            case 146: {
                Wat2WasmModuleMachineFuncGroup_0.func_146(n, n2, n3, n4, memory, instance);
                return;
            }
            case 201: {
                Wat2WasmModuleMachineFuncGroup_0.func_201(n, n2, n3, n4, memory, instance);
                return;
            }
            case 206: {
                Wat2WasmModuleMachineFuncGroup_0.func_206(n, n2, n3, n4, memory, instance);
                return;
            }
            case 207: {
                Wat2WasmModuleMachineFuncGroup_0.func_207(n, n2, n3, n4, memory, instance);
                return;
            }
            case 225: {
                Wat2WasmModuleMachineFuncGroup_0.func_225(n, n2, n3, n4, memory, instance);
                return;
            }
            case 451: {
                Wat2WasmModuleMachineFuncGroup_0.func_451(n, n2, n3, n4, memory, instance);
                return;
            }
            case 452: {
                Wat2WasmModuleMachineFuncGroup_0.func_452(n, n2, n3, n4, memory, instance);
                return;
            }
            case 459: {
                Wat2WasmModuleMachineFuncGroup_0.func_459(n, n2, n3, n4, memory, instance);
                return;
            }
            case 564: {
                Wat2WasmModuleMachineFuncGroup_0.func_564(n, n2, n3, n4, memory, instance);
                return;
            }
            case 566: {
                Wat2WasmModuleMachineFuncGroup_0.func_566(n, n2, n3, n4, memory, instance);
                return;
            }
            case 569: {
                Wat2WasmModuleMachineFuncGroup_0.func_569(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1086: {
                Wat2WasmModuleMachineFuncGroup_0.func_1086(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1100: {
                Wat2WasmModuleMachineFuncGroup_0.func_1100(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1151: {
                Wat2WasmModuleMachineFuncGroup_0.func_1151(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1193: {
                Wat2WasmModuleMachineFuncGroup_0.func_1193(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1208: {
                Wat2WasmModuleMachineFuncGroup_0.func_1208(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1309: {
                Wat2WasmModuleMachineFuncGroup_0.func_1309(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1666: {
                Wat2WasmModuleMachineFuncGroup_0.func_1666(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1670: {
                Wat2WasmModuleMachineFuncGroup_0.func_1670(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1686: {
                Wat2WasmModuleMachineFuncGroup_0.func_1686(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1687: {
                Wat2WasmModuleMachineFuncGroup_0.func_1687(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1688: {
                Wat2WasmModuleMachineFuncGroup_0.func_1688(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1696: {
                Wat2WasmModuleMachineFuncGroup_0.func_1696(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1718: {
                Wat2WasmModuleMachineFuncGroup_0.func_1718(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1722: {
                Wat2WasmModuleMachineFuncGroup_0.func_1722(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1758: {
                Wat2WasmModuleMachineFuncGroup_0.func_1758(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1759: {
                Wat2WasmModuleMachineFuncGroup_0.func_1759(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1760: {
                Wat2WasmModuleMachineFuncGroup_0.func_1760(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1761: {
                Wat2WasmModuleMachineFuncGroup_0.func_1761(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1763: {
                Wat2WasmModuleMachineFuncGroup_0.func_1763(n, n2, n3, n4, memory, instance);
                return;
            }
            case 1765: {
                Wat2WasmModuleMachineFuncGroup_0.func_1765(n, n2, n3, n4, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_17(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n8);
        final int requiredRef = table.requiredRef(n7);
        final Instance instance2 = table.instance(n7);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6 }, 17, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 210: {
                Wat2WasmModuleMachineFuncGroup_0.func_210(n, n2, n3, n4, n5, n6, memory, instance);
                return;
            }
            case 221: {
                Wat2WasmModuleMachineFuncGroup_0.func_221(n, n2, n3, n4, n5, n6, memory, instance);
                return;
            }
            case 1767: {
                Wat2WasmModuleMachineFuncGroup_0.func_1767(n, n2, n3, n4, n5, n6, memory, instance);
                return;
            }
            case 1771: {
                Wat2WasmModuleMachineFuncGroup_0.func_1771(n, n2, n3, n4, n5, n6, memory, instance);
                return;
            }
            case 1772: {
                Wat2WasmModuleMachineFuncGroup_0.func_1772(n, n2, n3, n4, n5, n6, memory, instance);
                return;
            }
            case 1773: {
                Wat2WasmModuleMachineFuncGroup_0.func_1773(n, n2, n3, n4, n5, n6, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_18(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n7);
        final int requiredRef = table.requiredRef(n6);
        final Instance instance2 = table.instance(n6);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5 }, 18, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 118: {
                Wat2WasmModuleMachineFuncGroup_0.func_118(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 208: {
                Wat2WasmModuleMachineFuncGroup_0.func_208(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 224: {
                Wat2WasmModuleMachineFuncGroup_0.func_224(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 249: {
                Wat2WasmModuleMachineFuncGroup_0.func_249(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 253: {
                Wat2WasmModuleMachineFuncGroup_0.func_253(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 1310: {
                Wat2WasmModuleMachineFuncGroup_0.func_1310(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 1691: {
                Wat2WasmModuleMachineFuncGroup_0.func_1691(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 1764: {
                Wat2WasmModuleMachineFuncGroup_0.func_1764(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 1766: {
                Wat2WasmModuleMachineFuncGroup_0.func_1766(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 1768: {
                Wat2WasmModuleMachineFuncGroup_0.func_1768(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 1769: {
                Wat2WasmModuleMachineFuncGroup_0.func_1769(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 1770: {
                Wat2WasmModuleMachineFuncGroup_0.func_1770(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            case 1873: {
                Wat2WasmModuleMachineFuncGroup_0.func_1873(n, n2, n3, n4, n5, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_19(final int n, final int n2, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n2);
        final int requiredRef = table.requiredRef(n);
        final Instance instance2 = table.instance(n);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[0], 19, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 15: {
                Wat2WasmModuleMachineFuncGroup_0.func_15(memory, instance);
                return;
            }
            case 16: {
                Wat2WasmModuleMachineFuncGroup_0.func_16(memory, instance);
                return;
            }
            case 18: {
                Wat2WasmModuleMachineFuncGroup_0.func_18(memory, instance);
                return;
            }
            case 35: {
                Wat2WasmModuleMachineFuncGroup_0.func_35(memory, instance);
                return;
            }
            case 45: {
                Wat2WasmModuleMachineFuncGroup_0.func_45(memory, instance);
                return;
            }
            case 46: {
                Wat2WasmModuleMachineFuncGroup_0.func_46(memory, instance);
                return;
            }
            case 74: {
                Wat2WasmModuleMachineFuncGroup_0.func_74(memory, instance);
                return;
            }
            case 75: {
                Wat2WasmModuleMachineFuncGroup_0.func_75(memory, instance);
                return;
            }
            case 76: {
                Wat2WasmModuleMachineFuncGroup_0.func_76(memory, instance);
                return;
            }
            case 85: {
                Wat2WasmModuleMachineFuncGroup_0.func_85(memory, instance);
                return;
            }
            case 215: {
                Wat2WasmModuleMachineFuncGroup_0.func_215(memory, instance);
                return;
            }
            case 1152: {
                Wat2WasmModuleMachineFuncGroup_0.func_1152(memory, instance);
                return;
            }
            case 1807: {
                Wat2WasmModuleMachineFuncGroup_0.func_1807(memory, instance);
                return;
            }
            case 1817: {
                Wat2WasmModuleMachineFuncGroup_0.func_1817(memory, instance);
                return;
            }
            case 1820: {
                Wat2WasmModuleMachineFuncGroup_0.func_1820(memory, instance);
                return;
            }
            case 1821: {
                Wat2WasmModuleMachineFuncGroup_0.func_1821(memory, instance);
                return;
            }
            case 1824: {
                Wat2WasmModuleMachineFuncGroup_0.func_1824(memory, instance);
                return;
            }
            case 1826: {
                Wat2WasmModuleMachineFuncGroup_0.func_1826(memory, instance);
                return;
            }
            case 1849: {
                Wat2WasmModuleMachineFuncGroup_0.func_1849(memory, instance);
                return;
            }
            case 1858: {
                Wat2WasmModuleMachineFuncGroup_0.func_1858(memory, instance);
                return;
            }
            case 1874: {
                Wat2WasmModuleMachineFuncGroup_0.func_1874(memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static long call_indirect_20(final int n, final long n2, final int n3, final int n4, final int n5, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n5);
        final int requiredRef = table.requiredRef(n4);
        final Instance instance2 = table.instance(n4);
        if (instance2 != null && instance2 != instance) {
            return Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3 }, 20, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 1837: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1837(n, n2, n3, memory, instance);
            }
            case 1838: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1838(n, n2, n3, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_21(final int n, final long n2, final int n3, final int n4, final int n5, final int n6, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n6);
        final int requiredRef = table.requiredRef(n5);
        final Instance instance2 = table.instance(n5);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4 }, 21, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 10: {
                return Wat2WasmModuleMachineFuncGroup_0.func_10(n, n2, n3, n4, memory, instance);
            }
            case 1802: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1802(n, n2, n3, n4, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_22(final int n, final int n2, final int n3, final int n4, final int n5, final long n6, final long n7, final int n8, final int n9, final int n10, final int n11, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n11);
        final int requiredRef = table.requiredRef(n10);
        final Instance instance2 = table.instance(n10);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6, n7, n8, n9 }, 22, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 13: {
                return Wat2WasmModuleMachineFuncGroup_0.func_13(n, n2, n3, n4, n5, n6, n7, n8, n9, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_23(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9, final int n10, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n10);
        final int requiredRef = table.requiredRef(n9);
        final Instance instance2 = table.instance(n9);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6, n7, n8 }, 23, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 87: {
                Wat2WasmModuleMachineFuncGroup_0.func_87(n, n2, n3, n4, n5, n6, n7, n8, memory, instance);
                return;
            }
            case 1598: {
                Wat2WasmModuleMachineFuncGroup_0.func_1598(n, n2, n3, n4, n5, n6, n7, n8, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_24(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n9);
        final int requiredRef = table.requiredRef(n8);
        final Instance instance2 = table.instance(n8);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6, n7 }, 24, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 88: {
                Wat2WasmModuleMachineFuncGroup_0.func_88(n, n2, n3, n4, n5, n6, n7, memory, instance);
                return;
            }
            case 222: {
                Wat2WasmModuleMachineFuncGroup_0.func_222(n, n2, n3, n4, n5, n6, n7, memory, instance);
                return;
            }
            case 1643: {
                Wat2WasmModuleMachineFuncGroup_0.func_1643(n, n2, n3, n4, n5, n6, n7, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_25(final int n, final long n2, final int n3, final int n4, final int n5, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n5);
        final int requiredRef = table.requiredRef(n4);
        final Instance instance2 = table.instance(n4);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3 }, 25, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 174: {
                Wat2WasmModuleMachineFuncGroup_0.func_174(n, n2, n3, memory, instance);
                return;
            }
            case 175: {
                Wat2WasmModuleMachineFuncGroup_0.func_175(n, n2, n3, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static long call_indirect_26(final int n, final long n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            return Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2 }, 26, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 193: {
                return Wat2WasmModuleMachineFuncGroup_0.func_193(n, n2, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_27(final int n, final int n2, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n2);
        final int requiredRef = table.requiredRef(n);
        final Instance instance2 = table.instance(n);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[0], 27, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 240: {
                return Wat2WasmModuleMachineFuncGroup_0.func_240(memory, instance);
            }
            case 241: {
                return Wat2WasmModuleMachineFuncGroup_0.func_241(memory, instance);
            }
            case 1631: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1631(memory, instance);
            }
            case 1677: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1677(memory, instance);
            }
            case 1775: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1775(memory, instance);
            }
            case 1791: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1791(memory, instance);
            }
            case 1857: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1857(memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_28(final int n, final int n2, final int n3, final int n4, final long n5, final long n6, final int n7, final int n8, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n8);
        final int requiredRef = table.requiredRef(n7);
        final Instance instance2 = table.instance(n7);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6 }, 28, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 374: {
                return Wat2WasmModuleMachineFuncGroup_0.func_374(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 375: {
                return Wat2WasmModuleMachineFuncGroup_0.func_375(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 376: {
                return Wat2WasmModuleMachineFuncGroup_0.func_376(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 377: {
                return Wat2WasmModuleMachineFuncGroup_0.func_377(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 378: {
                return Wat2WasmModuleMachineFuncGroup_0.func_378(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 379: {
                return Wat2WasmModuleMachineFuncGroup_0.func_379(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 403: {
                return Wat2WasmModuleMachineFuncGroup_0.func_403(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 404: {
                return Wat2WasmModuleMachineFuncGroup_0.func_404(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 405: {
                return Wat2WasmModuleMachineFuncGroup_0.func_405(n, n2, n3, n4, n5, n6, memory, instance);
            }
            case 429: {
                return Wat2WasmModuleMachineFuncGroup_0.func_429(n, n2, n3, n4, n5, n6, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_29(final int n, final int n2, final int n3, final int n4, final long n5, final long n6, final long n7, final int n8, final int n9, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n9);
        final int requiredRef = table.requiredRef(n8);
        final Instance instance2 = table.instance(n8);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6, n7 }, 29, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 426: {
                return Wat2WasmModuleMachineFuncGroup_0.func_426(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            case 427: {
                return Wat2WasmModuleMachineFuncGroup_0.func_427(n, n2, n3, n4, n5, n6, n7, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_30(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9, final int n10, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n10);
        final int requiredRef = table.requiredRef(n9);
        final Instance instance2 = table.instance(n9);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6, n7, n8 }, 30, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 827: {
                return Wat2WasmModuleMachineFuncGroup_0.func_827(n, n2, n3, n4, n5, n6, n7, n8, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_31(final int n, final long n2, final int n3, final long n4, final int n5, final int n6, final int n7, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n7);
        final int requiredRef = table.requiredRef(n6);
        final Instance instance2 = table.instance(n6);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5 }, 31, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 829: {
                return Wat2WasmModuleMachineFuncGroup_0.func_829(n, n2, n3, n4, n5, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static int call_indirect_32(final int n, final int n2, final int n3, final int n4, final long n5, final long n6, final int n7, final int n8, final int n9, final int n10, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n10);
        final int requiredRef = table.requiredRef(n9);
        final Instance instance2 = table.instance(n9);
        if (instance2 != null && instance2 != instance) {
            return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5, n6, n7, n8 }, 32, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 1805: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1805(n, n2, n3, n4, n5, n6, n7, n8, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static double call_indirect_33(final double n, final int n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { Value.doubleToLong(n), n2 }, 33, requiredRef, instance2)[0]);
        }
        switch (requiredRef) {
            case 1869: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1869(n, n2, memory, instance);
            }
            case 1881: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1881(n, n2, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static void call_indirect_34(final int n, final long n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2 }, 34, requiredRef, instance2);
            return;
        }
        switch (requiredRef) {
            case 1879: {
                Wat2WasmModuleMachineFuncGroup_0.func_1879(n, n2, memory, instance);
                return;
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static double call_indirect_35(final double n, final double n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { Value.doubleToLong(n), Value.doubleToLong(n2) }, 35, requiredRef, instance2)[0]);
        }
        switch (requiredRef) {
            case 1882: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1882(n, n2, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static double call_indirect_36(final int n, final int n2, final int n3, final int n4, final int n5, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n5);
        final int requiredRef = table.requiredRef(n4);
        final Instance instance2 = table.instance(n4);
        if (instance2 != null && instance2 != instance) {
            return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3 }, 36, requiredRef, instance2)[0]);
        }
        switch (requiredRef) {
            case 1883: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1883(n, n2, n3, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static double call_indirect_37(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n7);
        final int requiredRef = table.requiredRef(n6);
        final Instance instance2 = table.instance(n6);
        if (instance2 != null && instance2 != instance) {
            return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2, n3, n4, n5 }, 37, requiredRef, instance2)[0]);
        }
        switch (requiredRef) {
            case 1884: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1884(n, n2, n3, n4, n5, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static long call_indirect_38(final int n, final int n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            return Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2 }, 38, requiredRef, instance2)[0];
        }
        switch (requiredRef) {
            case 1885: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1885(n, n2, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static float call_indirect_39(final int n, final int n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            return Value.longToFloat(Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2 }, 39, requiredRef, instance2)[0]);
        }
        switch (requiredRef) {
            case 1886: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1886(n, n2, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
    
    public static double call_indirect_40(final int n, final int n2, final int n3, final int n4, final Memory memory, final Instance instance) {
        Wat2WasmModuleMachineShaded.checkInterruption();
        final TableInstance table = instance.table(n4);
        final int requiredRef = table.requiredRef(n3);
        final Instance instance2 = table.instance(n3);
        if (instance2 != null && instance2 != instance) {
            return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { n, n2 }, 40, requiredRef, instance2)[0]);
        }
        switch (requiredRef) {
            case 1887: {
                return Wat2WasmModuleMachineFuncGroup_0.func_1887(n, n2, memory, instance);
            }
            default: {
                throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
            }
        }
    }
}
