package com.dylibso.chicory.wabt;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.wasm.types.Value;

public final class Wat2WasmModuleMachine implements Machine {
  private final Instance instance;
  
  public Wat2WasmModuleMachine(Instance paramInstance) {
    this.instance = paramInstance;
  }
  
  public long[] call(int paramInt, long[] paramArrayOflong) {
    try {
      return Wat2WasmModuleMachineMachineCall.call(this.instance, this.instance.memory(), paramInt, paramArrayOflong);
    } catch (StackOverflowError stackOverflowError) {
      throw Wat2WasmModuleMachineShaded.throwCallStackExhausted(null);
    } 
  }
  
  public static void call_indirect_0(int paramInt1, int paramInt2, int paramInt3, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt3);
    int i = tableInstance.requiredRef(paramInt2);
    Instance instance = tableInstance.instance(paramInt2);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 14:
          Wat2WasmModuleMachineFuncGroup_0.func_14(paramInt1, paramMemory, paramInstance);
          return;
        case 19:
          Wat2WasmModuleMachineFuncGroup_0.func_19(paramInt1, paramMemory, paramInstance);
          return;
        case 20:
          Wat2WasmModuleMachineFuncGroup_0.func_20(paramInt1, paramMemory, paramInstance);
          return;
        case 22:
          Wat2WasmModuleMachineFuncGroup_0.func_22(paramInt1, paramMemory, paramInstance);
          return;
        case 23:
          Wat2WasmModuleMachineFuncGroup_0.func_23(paramInt1, paramMemory, paramInstance);
          return;
        case 24:
          Wat2WasmModuleMachineFuncGroup_0.func_24(paramInt1, paramMemory, paramInstance);
          return;
        case 26:
          Wat2WasmModuleMachineFuncGroup_0.func_26(paramInt1, paramMemory, paramInstance);
          return;
        case 27:
          Wat2WasmModuleMachineFuncGroup_0.func_27(paramInt1, paramMemory, paramInstance);
          return;
        case 28:
          Wat2WasmModuleMachineFuncGroup_0.func_28(paramInt1, paramMemory, paramInstance);
          return;
        case 29:
          Wat2WasmModuleMachineFuncGroup_0.func_29(paramInt1, paramMemory, paramInstance);
          return;
        case 31:
          Wat2WasmModuleMachineFuncGroup_0.func_31(paramInt1, paramMemory, paramInstance);
          return;
        case 90:
          Wat2WasmModuleMachineFuncGroup_0.func_90(paramInt1, paramMemory, paramInstance);
          return;
        case 91:
          Wat2WasmModuleMachineFuncGroup_0.func_91(paramInt1, paramMemory, paramInstance);
          return;
        case 92:
          Wat2WasmModuleMachineFuncGroup_0.func_92(paramInt1, paramMemory, paramInstance);
          return;
        case 93:
          Wat2WasmModuleMachineFuncGroup_0.func_93(paramInt1, paramMemory, paramInstance);
          return;
        case 94:
          Wat2WasmModuleMachineFuncGroup_0.func_94(paramInt1, paramMemory, paramInstance);
          return;
        case 95:
          Wat2WasmModuleMachineFuncGroup_0.func_95(paramInt1, paramMemory, paramInstance);
          return;
        case 96:
          Wat2WasmModuleMachineFuncGroup_0.func_96(paramInt1, paramMemory, paramInstance);
          return;
        case 97:
          Wat2WasmModuleMachineFuncGroup_0.func_97(paramInt1, paramMemory, paramInstance);
          return;
        case 98:
          Wat2WasmModuleMachineFuncGroup_0.func_98(paramInt1, paramMemory, paramInstance);
          return;
        case 99:
          Wat2WasmModuleMachineFuncGroup_0.func_99(paramInt1, paramMemory, paramInstance);
          return;
        case 100:
          Wat2WasmModuleMachineFuncGroup_0.func_100(paramInt1, paramMemory, paramInstance);
          return;
        case 101:
          Wat2WasmModuleMachineFuncGroup_0.func_101(paramInt1, paramMemory, paramInstance);
          return;
        case 102:
          Wat2WasmModuleMachineFuncGroup_0.func_102(paramInt1, paramMemory, paramInstance);
          return;
        case 103:
          Wat2WasmModuleMachineFuncGroup_0.func_103(paramInt1, paramMemory, paramInstance);
          return;
        case 104:
          Wat2WasmModuleMachineFuncGroup_0.func_104(paramInt1, paramMemory, paramInstance);
          return;
        case 105:
          Wat2WasmModuleMachineFuncGroup_0.func_105(paramInt1, paramMemory, paramInstance);
          return;
        case 106:
          Wat2WasmModuleMachineFuncGroup_0.func_106(paramInt1, paramMemory, paramInstance);
          return;
        case 107:
          Wat2WasmModuleMachineFuncGroup_0.func_107(paramInt1, paramMemory, paramInstance);
          return;
        case 108:
          Wat2WasmModuleMachineFuncGroup_0.func_108(paramInt1, paramMemory, paramInstance);
          return;
        case 109:
          Wat2WasmModuleMachineFuncGroup_0.func_109(paramInt1, paramMemory, paramInstance);
          return;
        case 200:
          Wat2WasmModuleMachineFuncGroup_0.func_200(paramInt1, paramMemory, paramInstance);
          return;
        case 202:
          Wat2WasmModuleMachineFuncGroup_0.func_202(paramInt1, paramMemory, paramInstance);
          return;
        case 213:
          Wat2WasmModuleMachineFuncGroup_0.func_213(paramInt1, paramMemory, paramInstance);
          return;
        case 217:
          Wat2WasmModuleMachineFuncGroup_0.func_217(paramInt1, paramMemory, paramInstance);
          return;
        case 219:
          Wat2WasmModuleMachineFuncGroup_0.func_219(paramInt1, paramMemory, paramInstance);
          return;
        case 235:
          Wat2WasmModuleMachineFuncGroup_0.func_235(paramInt1, paramMemory, paramInstance);
          return;
        case 236:
          Wat2WasmModuleMachineFuncGroup_0.func_236(paramInt1, paramMemory, paramInstance);
          return;
        case 243:
          Wat2WasmModuleMachineFuncGroup_0.func_243(paramInt1, paramMemory, paramInstance);
          return;
        case 244:
          Wat2WasmModuleMachineFuncGroup_0.func_244(paramInt1, paramMemory, paramInstance);
          return;
        case 460:
          Wat2WasmModuleMachineFuncGroup_0.func_460(paramInt1, paramMemory, paramInstance);
          return;
        case 799:
          Wat2WasmModuleMachineFuncGroup_0.func_799(paramInt1, paramMemory, paramInstance);
          return;
        case 831:
          Wat2WasmModuleMachineFuncGroup_0.func_831(paramInt1, paramMemory, paramInstance);
          return;
        case 832:
          Wat2WasmModuleMachineFuncGroup_0.func_832(paramInt1, paramMemory, paramInstance);
          return;
        case 841:
          Wat2WasmModuleMachineFuncGroup_0.func_841(paramInt1, paramMemory, paramInstance);
          return;
        case 1005:
          Wat2WasmModuleMachineFuncGroup_0.func_1005(paramInt1, paramMemory, paramInstance);
          return;
        case 1084:
          Wat2WasmModuleMachineFuncGroup_0.func_1084(paramInt1, paramMemory, paramInstance);
          return;
        case 1088:
          Wat2WasmModuleMachineFuncGroup_0.func_1088(paramInt1, paramMemory, paramInstance);
          return;
        case 1089:
          Wat2WasmModuleMachineFuncGroup_0.func_1089(paramInt1, paramMemory, paramInstance);
          return;
        case 1090:
          Wat2WasmModuleMachineFuncGroup_0.func_1090(paramInt1, paramMemory, paramInstance);
          return;
        case 1092:
          Wat2WasmModuleMachineFuncGroup_0.func_1092(paramInt1, paramMemory, paramInstance);
          return;
        case 1103:
          Wat2WasmModuleMachineFuncGroup_0.func_1103(paramInt1, paramMemory, paramInstance);
          return;
        case 1217:
          Wat2WasmModuleMachineFuncGroup_0.func_1217(paramInt1, paramMemory, paramInstance);
          return;
        case 1312:
          Wat2WasmModuleMachineFuncGroup_0.func_1312(paramInt1, paramMemory, paramInstance);
          return;
        case 1314:
          Wat2WasmModuleMachineFuncGroup_0.func_1314(paramInt1, paramMemory, paramInstance);
          return;
        case 1316:
          Wat2WasmModuleMachineFuncGroup_0.func_1316(paramInt1, paramMemory, paramInstance);
          return;
        case 1317:
          Wat2WasmModuleMachineFuncGroup_0.func_1317(paramInt1, paramMemory, paramInstance);
          return;
        case 1394:
          Wat2WasmModuleMachineFuncGroup_0.func_1394(paramInt1, paramMemory, paramInstance);
          return;
        case 1395:
          Wat2WasmModuleMachineFuncGroup_0.func_1395(paramInt1, paramMemory, paramInstance);
          return;
        case 1397:
          Wat2WasmModuleMachineFuncGroup_0.func_1397(paramInt1, paramMemory, paramInstance);
          return;
        case 1398:
          Wat2WasmModuleMachineFuncGroup_0.func_1398(paramInt1, paramMemory, paramInstance);
          return;
        case 1400:
          Wat2WasmModuleMachineFuncGroup_0.func_1400(paramInt1, paramMemory, paramInstance);
          return;
        case 1402:
          Wat2WasmModuleMachineFuncGroup_0.func_1402(paramInt1, paramMemory, paramInstance);
          return;
        case 1403:
          Wat2WasmModuleMachineFuncGroup_0.func_1403(paramInt1, paramMemory, paramInstance);
          return;
        case 1405:
          Wat2WasmModuleMachineFuncGroup_0.func_1405(paramInt1, paramMemory, paramInstance);
          return;
        case 1406:
          Wat2WasmModuleMachineFuncGroup_0.func_1406(paramInt1, paramMemory, paramInstance);
          return;
        case 1411:
          Wat2WasmModuleMachineFuncGroup_0.func_1411(paramInt1, paramMemory, paramInstance);
          return;
        case 1415:
          Wat2WasmModuleMachineFuncGroup_0.func_1415(paramInt1, paramMemory, paramInstance);
          return;
        case 1417:
          Wat2WasmModuleMachineFuncGroup_0.func_1417(paramInt1, paramMemory, paramInstance);
          return;
        case 1419:
          Wat2WasmModuleMachineFuncGroup_0.func_1419(paramInt1, paramMemory, paramInstance);
          return;
        case 1421:
          Wat2WasmModuleMachineFuncGroup_0.func_1421(paramInt1, paramMemory, paramInstance);
          return;
        case 1422:
          Wat2WasmModuleMachineFuncGroup_0.func_1422(paramInt1, paramMemory, paramInstance);
          return;
        case 1424:
          Wat2WasmModuleMachineFuncGroup_0.func_1424(paramInt1, paramMemory, paramInstance);
          return;
        case 1426:
          Wat2WasmModuleMachineFuncGroup_0.func_1426(paramInt1, paramMemory, paramInstance);
          return;
        case 1428:
          Wat2WasmModuleMachineFuncGroup_0.func_1428(paramInt1, paramMemory, paramInstance);
          return;
        case 1431:
          Wat2WasmModuleMachineFuncGroup_0.func_1431(paramInt1, paramMemory, paramInstance);
          return;
        case 1433:
          Wat2WasmModuleMachineFuncGroup_0.func_1433(paramInt1, paramMemory, paramInstance);
          return;
        case 1435:
          Wat2WasmModuleMachineFuncGroup_0.func_1435(paramInt1, paramMemory, paramInstance);
          return;
        case 1437:
          Wat2WasmModuleMachineFuncGroup_0.func_1437(paramInt1, paramMemory, paramInstance);
          return;
        case 1439:
          Wat2WasmModuleMachineFuncGroup_0.func_1439(paramInt1, paramMemory, paramInstance);
          return;
        case 1441:
          Wat2WasmModuleMachineFuncGroup_0.func_1441(paramInt1, paramMemory, paramInstance);
          return;
        case 1443:
          Wat2WasmModuleMachineFuncGroup_0.func_1443(paramInt1, paramMemory, paramInstance);
          return;
        case 1446:
          Wat2WasmModuleMachineFuncGroup_0.func_1446(paramInt1, paramMemory, paramInstance);
          return;
        case 1448:
          Wat2WasmModuleMachineFuncGroup_0.func_1448(paramInt1, paramMemory, paramInstance);
          return;
        case 1450:
          Wat2WasmModuleMachineFuncGroup_0.func_1450(paramInt1, paramMemory, paramInstance);
          return;
        case 1452:
          Wat2WasmModuleMachineFuncGroup_0.func_1452(paramInt1, paramMemory, paramInstance);
          return;
        case 1454:
          Wat2WasmModuleMachineFuncGroup_0.func_1454(paramInt1, paramMemory, paramInstance);
          return;
        case 1455:
          Wat2WasmModuleMachineFuncGroup_0.func_1455(paramInt1, paramMemory, paramInstance);
          return;
        case 1456:
          Wat2WasmModuleMachineFuncGroup_0.func_1456(paramInt1, paramMemory, paramInstance);
          return;
        case 1457:
          Wat2WasmModuleMachineFuncGroup_0.func_1457(paramInt1, paramMemory, paramInstance);
          return;
        case 1459:
          Wat2WasmModuleMachineFuncGroup_0.func_1459(paramInt1, paramMemory, paramInstance);
          return;
        case 1461:
          Wat2WasmModuleMachineFuncGroup_0.func_1461(paramInt1, paramMemory, paramInstance);
          return;
        case 1463:
          Wat2WasmModuleMachineFuncGroup_0.func_1463(paramInt1, paramMemory, paramInstance);
          return;
        case 1464:
          Wat2WasmModuleMachineFuncGroup_0.func_1464(paramInt1, paramMemory, paramInstance);
          return;
        case 1466:
          Wat2WasmModuleMachineFuncGroup_0.func_1466(paramInt1, paramMemory, paramInstance);
          return;
        case 1468:
          Wat2WasmModuleMachineFuncGroup_0.func_1468(paramInt1, paramMemory, paramInstance);
          return;
        case 1470:
          Wat2WasmModuleMachineFuncGroup_0.func_1470(paramInt1, paramMemory, paramInstance);
          return;
        case 1472:
          Wat2WasmModuleMachineFuncGroup_0.func_1472(paramInt1, paramMemory, paramInstance);
          return;
        case 1474:
          Wat2WasmModuleMachineFuncGroup_0.func_1474(paramInt1, paramMemory, paramInstance);
          return;
        case 1476:
          Wat2WasmModuleMachineFuncGroup_0.func_1476(paramInt1, paramMemory, paramInstance);
          return;
        case 1478:
          Wat2WasmModuleMachineFuncGroup_0.func_1478(paramInt1, paramMemory, paramInstance);
          return;
        case 1480:
          Wat2WasmModuleMachineFuncGroup_0.func_1480(paramInt1, paramMemory, paramInstance);
          return;
        case 1482:
          Wat2WasmModuleMachineFuncGroup_0.func_1482(paramInt1, paramMemory, paramInstance);
          return;
        case 1483:
          Wat2WasmModuleMachineFuncGroup_0.func_1483(paramInt1, paramMemory, paramInstance);
          return;
        case 1485:
          Wat2WasmModuleMachineFuncGroup_0.func_1485(paramInt1, paramMemory, paramInstance);
          return;
        case 1486:
          Wat2WasmModuleMachineFuncGroup_0.func_1486(paramInt1, paramMemory, paramInstance);
          return;
        case 1488:
          Wat2WasmModuleMachineFuncGroup_0.func_1488(paramInt1, paramMemory, paramInstance);
          return;
        case 1489:
          Wat2WasmModuleMachineFuncGroup_0.func_1489(paramInt1, paramMemory, paramInstance);
          return;
        case 1490:
          Wat2WasmModuleMachineFuncGroup_0.func_1490(paramInt1, paramMemory, paramInstance);
          return;
        case 1491:
          Wat2WasmModuleMachineFuncGroup_0.func_1491(paramInt1, paramMemory, paramInstance);
          return;
        case 1492:
          Wat2WasmModuleMachineFuncGroup_0.func_1492(paramInt1, paramMemory, paramInstance);
          return;
        case 1494:
          Wat2WasmModuleMachineFuncGroup_0.func_1494(paramInt1, paramMemory, paramInstance);
          return;
        case 1496:
          Wat2WasmModuleMachineFuncGroup_0.func_1496(paramInt1, paramMemory, paramInstance);
          return;
        case 1498:
          Wat2WasmModuleMachineFuncGroup_0.func_1498(paramInt1, paramMemory, paramInstance);
          return;
        case 1500:
          Wat2WasmModuleMachineFuncGroup_0.func_1500(paramInt1, paramMemory, paramInstance);
          return;
        case 1502:
          Wat2WasmModuleMachineFuncGroup_0.func_1502(paramInt1, paramMemory, paramInstance);
          return;
        case 1504:
          Wat2WasmModuleMachineFuncGroup_0.func_1504(paramInt1, paramMemory, paramInstance);
          return;
        case 1506:
          Wat2WasmModuleMachineFuncGroup_0.func_1506(paramInt1, paramMemory, paramInstance);
          return;
        case 1508:
          Wat2WasmModuleMachineFuncGroup_0.func_1508(paramInt1, paramMemory, paramInstance);
          return;
        case 1510:
          Wat2WasmModuleMachineFuncGroup_0.func_1510(paramInt1, paramMemory, paramInstance);
          return;
        case 1512:
          Wat2WasmModuleMachineFuncGroup_0.func_1512(paramInt1, paramMemory, paramInstance);
          return;
        case 1514:
          Wat2WasmModuleMachineFuncGroup_0.func_1514(paramInt1, paramMemory, paramInstance);
          return;
        case 1516:
          Wat2WasmModuleMachineFuncGroup_0.func_1516(paramInt1, paramMemory, paramInstance);
          return;
        case 1518:
          Wat2WasmModuleMachineFuncGroup_0.func_1518(paramInt1, paramMemory, paramInstance);
          return;
        case 1519:
          Wat2WasmModuleMachineFuncGroup_0.func_1519(paramInt1, paramMemory, paramInstance);
          return;
        case 1520:
          Wat2WasmModuleMachineFuncGroup_0.func_1520(paramInt1, paramMemory, paramInstance);
          return;
        case 1522:
          Wat2WasmModuleMachineFuncGroup_0.func_1522(paramInt1, paramMemory, paramInstance);
          return;
        case 1523:
          Wat2WasmModuleMachineFuncGroup_0.func_1523(paramInt1, paramMemory, paramInstance);
          return;
        case 1525:
          Wat2WasmModuleMachineFuncGroup_0.func_1525(paramInt1, paramMemory, paramInstance);
          return;
        case 1527:
          Wat2WasmModuleMachineFuncGroup_0.func_1527(paramInt1, paramMemory, paramInstance);
          return;
        case 1528:
          Wat2WasmModuleMachineFuncGroup_0.func_1528(paramInt1, paramMemory, paramInstance);
          return;
        case 1530:
          Wat2WasmModuleMachineFuncGroup_0.func_1530(paramInt1, paramMemory, paramInstance);
          return;
        case 1531:
          Wat2WasmModuleMachineFuncGroup_0.func_1531(paramInt1, paramMemory, paramInstance);
          return;
        case 1533:
          Wat2WasmModuleMachineFuncGroup_0.func_1533(paramInt1, paramMemory, paramInstance);
          return;
        case 1534:
          Wat2WasmModuleMachineFuncGroup_0.func_1534(paramInt1, paramMemory, paramInstance);
          return;
        case 1536:
          Wat2WasmModuleMachineFuncGroup_0.func_1536(paramInt1, paramMemory, paramInstance);
          return;
        case 1537:
          Wat2WasmModuleMachineFuncGroup_0.func_1537(paramInt1, paramMemory, paramInstance);
          return;
        case 1539:
          Wat2WasmModuleMachineFuncGroup_0.func_1539(paramInt1, paramMemory, paramInstance);
          return;
        case 1540:
          Wat2WasmModuleMachineFuncGroup_0.func_1540(paramInt1, paramMemory, paramInstance);
          return;
        case 1542:
          Wat2WasmModuleMachineFuncGroup_0.func_1542(paramInt1, paramMemory, paramInstance);
          return;
        case 1543:
          Wat2WasmModuleMachineFuncGroup_0.func_1543(paramInt1, paramMemory, paramInstance);
          return;
        case 1544:
          Wat2WasmModuleMachineFuncGroup_0.func_1544(paramInt1, paramMemory, paramInstance);
          return;
        case 1546:
          Wat2WasmModuleMachineFuncGroup_0.func_1546(paramInt1, paramMemory, paramInstance);
          return;
        case 1547:
          Wat2WasmModuleMachineFuncGroup_0.func_1547(paramInt1, paramMemory, paramInstance);
          return;
        case 1549:
          Wat2WasmModuleMachineFuncGroup_0.func_1549(paramInt1, paramMemory, paramInstance);
          return;
        case 1550:
          Wat2WasmModuleMachineFuncGroup_0.func_1550(paramInt1, paramMemory, paramInstance);
          return;
        case 1552:
          Wat2WasmModuleMachineFuncGroup_0.func_1552(paramInt1, paramMemory, paramInstance);
          return;
        case 1554:
          Wat2WasmModuleMachineFuncGroup_0.func_1554(paramInt1, paramMemory, paramInstance);
          return;
        case 1556:
          Wat2WasmModuleMachineFuncGroup_0.func_1556(paramInt1, paramMemory, paramInstance);
          return;
        case 1558:
          Wat2WasmModuleMachineFuncGroup_0.func_1558(paramInt1, paramMemory, paramInstance);
          return;
        case 1560:
          Wat2WasmModuleMachineFuncGroup_0.func_1560(paramInt1, paramMemory, paramInstance);
          return;
        case 1562:
          Wat2WasmModuleMachineFuncGroup_0.func_1562(paramInt1, paramMemory, paramInstance);
          return;
        case 1564:
          Wat2WasmModuleMachineFuncGroup_0.func_1564(paramInt1, paramMemory, paramInstance);
          return;
        case 1567:
          Wat2WasmModuleMachineFuncGroup_0.func_1567(paramInt1, paramMemory, paramInstance);
          return;
        case 1570:
          Wat2WasmModuleMachineFuncGroup_0.func_1570(paramInt1, paramMemory, paramInstance);
          return;
        case 1574:
          Wat2WasmModuleMachineFuncGroup_0.func_1574(paramInt1, paramMemory, paramInstance);
          return;
        case 1576:
          Wat2WasmModuleMachineFuncGroup_0.func_1576(paramInt1, paramMemory, paramInstance);
          return;
        case 1591:
          Wat2WasmModuleMachineFuncGroup_0.func_1591(paramInt1, paramMemory, paramInstance);
          return;
        case 1602:
          Wat2WasmModuleMachineFuncGroup_0.func_1602(paramInt1, paramMemory, paramInstance);
          return;
        case 1612:
          Wat2WasmModuleMachineFuncGroup_0.func_1612(paramInt1, paramMemory, paramInstance);
          return;
        case 1618:
          Wat2WasmModuleMachineFuncGroup_0.func_1618(paramInt1, paramMemory, paramInstance);
          return;
        case 1632:
          Wat2WasmModuleMachineFuncGroup_0.func_1632(paramInt1, paramMemory, paramInstance);
          return;
        case 1655:
          Wat2WasmModuleMachineFuncGroup_0.func_1655(paramInt1, paramMemory, paramInstance);
          return;
        case 1712:
          Wat2WasmModuleMachineFuncGroup_0.func_1712(paramInt1, paramMemory, paramInstance);
          return;
        case 1742:
          Wat2WasmModuleMachineFuncGroup_0.func_1742(paramInt1, paramMemory, paramInstance);
          return;
        case 1743:
          Wat2WasmModuleMachineFuncGroup_0.func_1743(paramInt1, paramMemory, paramInstance);
          return;
        case 1745:
          Wat2WasmModuleMachineFuncGroup_0.func_1745(paramInt1, paramMemory, paramInstance);
          return;
        case 1747:
          Wat2WasmModuleMachineFuncGroup_0.func_1747(paramInt1, paramMemory, paramInstance);
          return;
        case 1749:
          Wat2WasmModuleMachineFuncGroup_0.func_1749(paramInt1, paramMemory, paramInstance);
          return;
        case 1751:
          Wat2WasmModuleMachineFuncGroup_0.func_1751(paramInt1, paramMemory, paramInstance);
          return;
        case 1777:
          Wat2WasmModuleMachineFuncGroup_0.func_1777(paramInt1, paramMemory, paramInstance);
          return;
        case 1781:
          Wat2WasmModuleMachineFuncGroup_0.func_1781(paramInt1, paramMemory, paramInstance);
          return;
        case 1784:
          Wat2WasmModuleMachineFuncGroup_0.func_1784(paramInt1, paramMemory, paramInstance);
          return;
        case 1785:
          Wat2WasmModuleMachineFuncGroup_0.func_1785(paramInt1, paramMemory, paramInstance);
          return;
        case 1790:
          Wat2WasmModuleMachineFuncGroup_0.func_1790(paramInt1, paramMemory, paramInstance);
          return;
        case 1806:
          Wat2WasmModuleMachineFuncGroup_0.func_1806(paramInt1, paramMemory, paramInstance);
          return;
        case 1827:
          Wat2WasmModuleMachineFuncGroup_0.func_1827(paramInt1, paramMemory, paramInstance);
          return;
        case 1830:
          Wat2WasmModuleMachineFuncGroup_0.func_1830(paramInt1, paramMemory, paramInstance);
          return;
        case 1862:
          Wat2WasmModuleMachineFuncGroup_0.func_1862(paramInt1, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[1])[0] = paramInt1;
  }
  
  public static int call_indirect_1(int paramInt1, int paramInt2, int paramInt3, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt3);
    int i = tableInstance.requiredRef(paramInt2);
    Instance instance = tableInstance.instance(paramInt2);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 4:
          return Wat2WasmModuleMachineFuncGroup_0.func_4(paramInt1, paramMemory, paramInstance);
        case 32:
          return Wat2WasmModuleMachineFuncGroup_0.func_32(paramInt1, paramMemory, paramInstance);
        case 41:
          return Wat2WasmModuleMachineFuncGroup_0.func_41(paramInt1, paramMemory, paramInstance);
        case 78:
          return Wat2WasmModuleMachineFuncGroup_0.func_78(paramInt1, paramMemory, paramInstance);
        case 79:
          return Wat2WasmModuleMachineFuncGroup_0.func_79(paramInt1, paramMemory, paramInstance);
        case 80:
          return Wat2WasmModuleMachineFuncGroup_0.func_80(paramInt1, paramMemory, paramInstance);
        case 130:
          return Wat2WasmModuleMachineFuncGroup_0.func_130(paramInt1, paramMemory, paramInstance);
        case 154:
          return Wat2WasmModuleMachineFuncGroup_0.func_154(paramInt1, paramMemory, paramInstance);
        case 162:
          return Wat2WasmModuleMachineFuncGroup_0.func_162(paramInt1, paramMemory, paramInstance);
        case 167:
          return Wat2WasmModuleMachineFuncGroup_0.func_167(paramInt1, paramMemory, paramInstance);
        case 186:
          return Wat2WasmModuleMachineFuncGroup_0.func_186(paramInt1, paramMemory, paramInstance);
        case 195:
          return Wat2WasmModuleMachineFuncGroup_0.func_195(paramInt1, paramMemory, paramInstance);
        case 216:
          return Wat2WasmModuleMachineFuncGroup_0.func_216(paramInt1, paramMemory, paramInstance);
        case 218:
          return Wat2WasmModuleMachineFuncGroup_0.func_218(paramInt1, paramMemory, paramInstance);
        case 228:
          return Wat2WasmModuleMachineFuncGroup_0.func_228(paramInt1, paramMemory, paramInstance);
        case 230:
          return Wat2WasmModuleMachineFuncGroup_0.func_230(paramInt1, paramMemory, paramInstance);
        case 234:
          return Wat2WasmModuleMachineFuncGroup_0.func_234(paramInt1, paramMemory, paramInstance);
        case 242:
          return Wat2WasmModuleMachineFuncGroup_0.func_242(paramInt1, paramMemory, paramInstance);
        case 273:
          return Wat2WasmModuleMachineFuncGroup_0.func_273(paramInt1, paramMemory, paramInstance);
        case 275:
          return Wat2WasmModuleMachineFuncGroup_0.func_275(paramInt1, paramMemory, paramInstance);
        case 286:
          return Wat2WasmModuleMachineFuncGroup_0.func_286(paramInt1, paramMemory, paramInstance);
        case 287:
          return Wat2WasmModuleMachineFuncGroup_0.func_287(paramInt1, paramMemory, paramInstance);
        case 289:
          return Wat2WasmModuleMachineFuncGroup_0.func_289(paramInt1, paramMemory, paramInstance);
        case 314:
          return Wat2WasmModuleMachineFuncGroup_0.func_314(paramInt1, paramMemory, paramInstance);
        case 317:
          return Wat2WasmModuleMachineFuncGroup_0.func_317(paramInt1, paramMemory, paramInstance);
        case 318:
          return Wat2WasmModuleMachineFuncGroup_0.func_318(paramInt1, paramMemory, paramInstance);
        case 331:
          return Wat2WasmModuleMachineFuncGroup_0.func_331(paramInt1, paramMemory, paramInstance);
        case 332:
          return Wat2WasmModuleMachineFuncGroup_0.func_332(paramInt1, paramMemory, paramInstance);
        case 334:
          return Wat2WasmModuleMachineFuncGroup_0.func_334(paramInt1, paramMemory, paramInstance);
        case 364:
          return Wat2WasmModuleMachineFuncGroup_0.func_364(paramInt1, paramMemory, paramInstance);
        case 369:
          return Wat2WasmModuleMachineFuncGroup_0.func_369(paramInt1, paramMemory, paramInstance);
        case 455:
          return Wat2WasmModuleMachineFuncGroup_0.func_455(paramInt1, paramMemory, paramInstance);
        case 456:
          return Wat2WasmModuleMachineFuncGroup_0.func_456(paramInt1, paramMemory, paramInstance);
        case 458:
          return Wat2WasmModuleMachineFuncGroup_0.func_458(paramInt1, paramMemory, paramInstance);
        case 538:
          return Wat2WasmModuleMachineFuncGroup_0.func_538(paramInt1, paramMemory, paramInstance);
        case 568:
          return Wat2WasmModuleMachineFuncGroup_0.func_568(paramInt1, paramMemory, paramInstance);
        case 642:
          return Wat2WasmModuleMachineFuncGroup_0.func_642(paramInt1, paramMemory, paramInstance);
        case 643:
          return Wat2WasmModuleMachineFuncGroup_0.func_643(paramInt1, paramMemory, paramInstance);
        case 646:
          return Wat2WasmModuleMachineFuncGroup_0.func_646(paramInt1, paramMemory, paramInstance);
        case 649:
          return Wat2WasmModuleMachineFuncGroup_0.func_649(paramInt1, paramMemory, paramInstance);
        case 653:
          return Wat2WasmModuleMachineFuncGroup_0.func_653(paramInt1, paramMemory, paramInstance);
        case 656:
          return Wat2WasmModuleMachineFuncGroup_0.func_656(paramInt1, paramMemory, paramInstance);
        case 659:
          return Wat2WasmModuleMachineFuncGroup_0.func_659(paramInt1, paramMemory, paramInstance);
        case 665:
          return Wat2WasmModuleMachineFuncGroup_0.func_665(paramInt1, paramMemory, paramInstance);
        case 668:
          return Wat2WasmModuleMachineFuncGroup_0.func_668(paramInt1, paramMemory, paramInstance);
        case 671:
          return Wat2WasmModuleMachineFuncGroup_0.func_671(paramInt1, paramMemory, paramInstance);
        case 676:
          return Wat2WasmModuleMachineFuncGroup_0.func_676(paramInt1, paramMemory, paramInstance);
        case 687:
          return Wat2WasmModuleMachineFuncGroup_0.func_687(paramInt1, paramMemory, paramInstance);
        case 689:
          return Wat2WasmModuleMachineFuncGroup_0.func_689(paramInt1, paramMemory, paramInstance);
        case 693:
          return Wat2WasmModuleMachineFuncGroup_0.func_693(paramInt1, paramMemory, paramInstance);
        case 694:
          return Wat2WasmModuleMachineFuncGroup_0.func_694(paramInt1, paramMemory, paramInstance);
        case 695:
          return Wat2WasmModuleMachineFuncGroup_0.func_695(paramInt1, paramMemory, paramInstance);
        case 718:
          return Wat2WasmModuleMachineFuncGroup_0.func_718(paramInt1, paramMemory, paramInstance);
        case 719:
          return Wat2WasmModuleMachineFuncGroup_0.func_719(paramInt1, paramMemory, paramInstance);
        case 723:
          return Wat2WasmModuleMachineFuncGroup_0.func_723(paramInt1, paramMemory, paramInstance);
        case 728:
          return Wat2WasmModuleMachineFuncGroup_0.func_728(paramInt1, paramMemory, paramInstance);
        case 729:
          return Wat2WasmModuleMachineFuncGroup_0.func_729(paramInt1, paramMemory, paramInstance);
        case 734:
          return Wat2WasmModuleMachineFuncGroup_0.func_734(paramInt1, paramMemory, paramInstance);
        case 743:
          return Wat2WasmModuleMachineFuncGroup_0.func_743(paramInt1, paramMemory, paramInstance);
        case 750:
          return Wat2WasmModuleMachineFuncGroup_0.func_750(paramInt1, paramMemory, paramInstance);
        case 753:
          return Wat2WasmModuleMachineFuncGroup_0.func_753(paramInt1, paramMemory, paramInstance);
        case 759:
          return Wat2WasmModuleMachineFuncGroup_0.func_759(paramInt1, paramMemory, paramInstance);
        case 761:
          return Wat2WasmModuleMachineFuncGroup_0.func_761(paramInt1, paramMemory, paramInstance);
        case 766:
          return Wat2WasmModuleMachineFuncGroup_0.func_766(paramInt1, paramMemory, paramInstance);
        case 769:
          return Wat2WasmModuleMachineFuncGroup_0.func_769(paramInt1, paramMemory, paramInstance);
        case 775:
          return Wat2WasmModuleMachineFuncGroup_0.func_775(paramInt1, paramMemory, paramInstance);
        case 777:
          return Wat2WasmModuleMachineFuncGroup_0.func_777(paramInt1, paramMemory, paramInstance);
        case 781:
          return Wat2WasmModuleMachineFuncGroup_0.func_781(paramInt1, paramMemory, paramInstance);
        case 784:
          return Wat2WasmModuleMachineFuncGroup_0.func_784(paramInt1, paramMemory, paramInstance);
        case 786:
          return Wat2WasmModuleMachineFuncGroup_0.func_786(paramInt1, paramMemory, paramInstance);
        case 800:
          return Wat2WasmModuleMachineFuncGroup_0.func_800(paramInt1, paramMemory, paramInstance);
        case 839:
          return Wat2WasmModuleMachineFuncGroup_0.func_839(paramInt1, paramMemory, paramInstance);
        case 840:
          return Wat2WasmModuleMachineFuncGroup_0.func_840(paramInt1, paramMemory, paramInstance);
        case 846:
          return Wat2WasmModuleMachineFuncGroup_0.func_846(paramInt1, paramMemory, paramInstance);
        case 849:
          return Wat2WasmModuleMachineFuncGroup_0.func_849(paramInt1, paramMemory, paramInstance);
        case 857:
          return Wat2WasmModuleMachineFuncGroup_0.func_857(paramInt1, paramMemory, paramInstance);
        case 867:
          return Wat2WasmModuleMachineFuncGroup_0.func_867(paramInt1, paramMemory, paramInstance);
        case 871:
          return Wat2WasmModuleMachineFuncGroup_0.func_871(paramInt1, paramMemory, paramInstance);
        case 875:
          return Wat2WasmModuleMachineFuncGroup_0.func_875(paramInt1, paramMemory, paramInstance);
        case 879:
          return Wat2WasmModuleMachineFuncGroup_0.func_879(paramInt1, paramMemory, paramInstance);
        case 887:
          return Wat2WasmModuleMachineFuncGroup_0.func_887(paramInt1, paramMemory, paramInstance);
        case 891:
          return Wat2WasmModuleMachineFuncGroup_0.func_891(paramInt1, paramMemory, paramInstance);
        case 894:
          return Wat2WasmModuleMachineFuncGroup_0.func_894(paramInt1, paramMemory, paramInstance);
        case 901:
          return Wat2WasmModuleMachineFuncGroup_0.func_901(paramInt1, paramMemory, paramInstance);
        case 903:
          return Wat2WasmModuleMachineFuncGroup_0.func_903(paramInt1, paramMemory, paramInstance);
        case 932:
          return Wat2WasmModuleMachineFuncGroup_0.func_932(paramInt1, paramMemory, paramInstance);
        case 935:
          return Wat2WasmModuleMachineFuncGroup_0.func_935(paramInt1, paramMemory, paramInstance);
        case 939:
          return Wat2WasmModuleMachineFuncGroup_0.func_939(paramInt1, paramMemory, paramInstance);
        case 940:
          return Wat2WasmModuleMachineFuncGroup_0.func_940(paramInt1, paramMemory, paramInstance);
        case 941:
          return Wat2WasmModuleMachineFuncGroup_0.func_941(paramInt1, paramMemory, paramInstance);
        case 971:
          return Wat2WasmModuleMachineFuncGroup_0.func_971(paramInt1, paramMemory, paramInstance);
        case 972:
          return Wat2WasmModuleMachineFuncGroup_0.func_972(paramInt1, paramMemory, paramInstance);
        case 974:
          return Wat2WasmModuleMachineFuncGroup_0.func_974(paramInt1, paramMemory, paramInstance);
        case 978:
          return Wat2WasmModuleMachineFuncGroup_0.func_978(paramInt1, paramMemory, paramInstance);
        case 981:
          return Wat2WasmModuleMachineFuncGroup_0.func_981(paramInt1, paramMemory, paramInstance);
        case 987:
          return Wat2WasmModuleMachineFuncGroup_0.func_987(paramInt1, paramMemory, paramInstance);
        case 989:
          return Wat2WasmModuleMachineFuncGroup_0.func_989(paramInt1, paramMemory, paramInstance);
        case 1008:
          return Wat2WasmModuleMachineFuncGroup_0.func_1008(paramInt1, paramMemory, paramInstance);
        case 1016:
          return Wat2WasmModuleMachineFuncGroup_0.func_1016(paramInt1, paramMemory, paramInstance);
        case 1019:
          return Wat2WasmModuleMachineFuncGroup_0.func_1019(paramInt1, paramMemory, paramInstance);
        case 1038:
          return Wat2WasmModuleMachineFuncGroup_0.func_1038(paramInt1, paramMemory, paramInstance);
        case 1042:
          return Wat2WasmModuleMachineFuncGroup_0.func_1042(paramInt1, paramMemory, paramInstance);
        case 1051:
          return Wat2WasmModuleMachineFuncGroup_0.func_1051(paramInt1, paramMemory, paramInstance);
        case 1055:
          return Wat2WasmModuleMachineFuncGroup_0.func_1055(paramInt1, paramMemory, paramInstance);
        case 1059:
          return Wat2WasmModuleMachineFuncGroup_0.func_1059(paramInt1, paramMemory, paramInstance);
        case 1075:
          return Wat2WasmModuleMachineFuncGroup_0.func_1075(paramInt1, paramMemory, paramInstance);
        case 1079:
          return Wat2WasmModuleMachineFuncGroup_0.func_1079(paramInt1, paramMemory, paramInstance);
        case 1085:
          return Wat2WasmModuleMachineFuncGroup_0.func_1085(paramInt1, paramMemory, paramInstance);
        case 1087:
          return Wat2WasmModuleMachineFuncGroup_0.func_1087(paramInt1, paramMemory, paramInstance);
        case 1091:
          return Wat2WasmModuleMachineFuncGroup_0.func_1091(paramInt1, paramMemory, paramInstance);
        case 1102:
          return Wat2WasmModuleMachineFuncGroup_0.func_1102(paramInt1, paramMemory, paramInstance);
        case 1156:
          return Wat2WasmModuleMachineFuncGroup_0.func_1156(paramInt1, paramMemory, paramInstance);
        case 1157:
          return Wat2WasmModuleMachineFuncGroup_0.func_1157(paramInt1, paramMemory, paramInstance);
        case 1169:
          return Wat2WasmModuleMachineFuncGroup_0.func_1169(paramInt1, paramMemory, paramInstance);
        case 1211:
          return Wat2WasmModuleMachineFuncGroup_0.func_1211(paramInt1, paramMemory, paramInstance);
        case 1239:
          return Wat2WasmModuleMachineFuncGroup_0.func_1239(paramInt1, paramMemory, paramInstance);
        case 1241:
          return Wat2WasmModuleMachineFuncGroup_0.func_1241(paramInt1, paramMemory, paramInstance);
        case 1244:
          return Wat2WasmModuleMachineFuncGroup_0.func_1244(paramInt1, paramMemory, paramInstance);
        case 1245:
          return Wat2WasmModuleMachineFuncGroup_0.func_1245(paramInt1, paramMemory, paramInstance);
        case 1246:
          return Wat2WasmModuleMachineFuncGroup_0.func_1246(paramInt1, paramMemory, paramInstance);
        case 1247:
          return Wat2WasmModuleMachineFuncGroup_0.func_1247(paramInt1, paramMemory, paramInstance);
        case 1248:
          return Wat2WasmModuleMachineFuncGroup_0.func_1248(paramInt1, paramMemory, paramInstance);
        case 1250:
          return Wat2WasmModuleMachineFuncGroup_0.func_1250(paramInt1, paramMemory, paramInstance);
        case 1251:
          return Wat2WasmModuleMachineFuncGroup_0.func_1251(paramInt1, paramMemory, paramInstance);
        case 1253:
          return Wat2WasmModuleMachineFuncGroup_0.func_1253(paramInt1, paramMemory, paramInstance);
        case 1256:
          return Wat2WasmModuleMachineFuncGroup_0.func_1256(paramInt1, paramMemory, paramInstance);
        case 1262:
          return Wat2WasmModuleMachineFuncGroup_0.func_1262(paramInt1, paramMemory, paramInstance);
        case 1264:
          return Wat2WasmModuleMachineFuncGroup_0.func_1264(paramInt1, paramMemory, paramInstance);
        case 1266:
          return Wat2WasmModuleMachineFuncGroup_0.func_1266(paramInt1, paramMemory, paramInstance);
        case 1299:
          return Wat2WasmModuleMachineFuncGroup_0.func_1299(paramInt1, paramMemory, paramInstance);
        case 1305:
          return Wat2WasmModuleMachineFuncGroup_0.func_1305(paramInt1, paramMemory, paramInstance);
        case 1311:
          return Wat2WasmModuleMachineFuncGroup_0.func_1311(paramInt1, paramMemory, paramInstance);
        case 1313:
          return Wat2WasmModuleMachineFuncGroup_0.func_1313(paramInt1, paramMemory, paramInstance);
        case 1315:
          return Wat2WasmModuleMachineFuncGroup_0.func_1315(paramInt1, paramMemory, paramInstance);
        case 1392:
          return Wat2WasmModuleMachineFuncGroup_0.func_1392(paramInt1, paramMemory, paramInstance);
        case 1393:
          return Wat2WasmModuleMachineFuncGroup_0.func_1393(paramInt1, paramMemory, paramInstance);
        case 1396:
          return Wat2WasmModuleMachineFuncGroup_0.func_1396(paramInt1, paramMemory, paramInstance);
        case 1399:
          return Wat2WasmModuleMachineFuncGroup_0.func_1399(paramInt1, paramMemory, paramInstance);
        case 1401:
          return Wat2WasmModuleMachineFuncGroup_0.func_1401(paramInt1, paramMemory, paramInstance);
        case 1404:
          return Wat2WasmModuleMachineFuncGroup_0.func_1404(paramInt1, paramMemory, paramInstance);
        case 1410:
          return Wat2WasmModuleMachineFuncGroup_0.func_1410(paramInt1, paramMemory, paramInstance);
        case 1414:
          return Wat2WasmModuleMachineFuncGroup_0.func_1414(paramInt1, paramMemory, paramInstance);
        case 1416:
          return Wat2WasmModuleMachineFuncGroup_0.func_1416(paramInt1, paramMemory, paramInstance);
        case 1418:
          return Wat2WasmModuleMachineFuncGroup_0.func_1418(paramInt1, paramMemory, paramInstance);
        case 1420:
          return Wat2WasmModuleMachineFuncGroup_0.func_1420(paramInt1, paramMemory, paramInstance);
        case 1423:
          return Wat2WasmModuleMachineFuncGroup_0.func_1423(paramInt1, paramMemory, paramInstance);
        case 1425:
          return Wat2WasmModuleMachineFuncGroup_0.func_1425(paramInt1, paramMemory, paramInstance);
        case 1427:
          return Wat2WasmModuleMachineFuncGroup_0.func_1427(paramInt1, paramMemory, paramInstance);
        case 1429:
          return Wat2WasmModuleMachineFuncGroup_0.func_1429(paramInt1, paramMemory, paramInstance);
        case 1430:
          return Wat2WasmModuleMachineFuncGroup_0.func_1430(paramInt1, paramMemory, paramInstance);
        case 1432:
          return Wat2WasmModuleMachineFuncGroup_0.func_1432(paramInt1, paramMemory, paramInstance);
        case 1434:
          return Wat2WasmModuleMachineFuncGroup_0.func_1434(paramInt1, paramMemory, paramInstance);
        case 1436:
          return Wat2WasmModuleMachineFuncGroup_0.func_1436(paramInt1, paramMemory, paramInstance);
        case 1438:
          return Wat2WasmModuleMachineFuncGroup_0.func_1438(paramInt1, paramMemory, paramInstance);
        case 1440:
          return Wat2WasmModuleMachineFuncGroup_0.func_1440(paramInt1, paramMemory, paramInstance);
        case 1442:
          return Wat2WasmModuleMachineFuncGroup_0.func_1442(paramInt1, paramMemory, paramInstance);
        case 1444:
          return Wat2WasmModuleMachineFuncGroup_0.func_1444(paramInt1, paramMemory, paramInstance);
        case 1445:
          return Wat2WasmModuleMachineFuncGroup_0.func_1445(paramInt1, paramMemory, paramInstance);
        case 1447:
          return Wat2WasmModuleMachineFuncGroup_0.func_1447(paramInt1, paramMemory, paramInstance);
        case 1449:
          return Wat2WasmModuleMachineFuncGroup_0.func_1449(paramInt1, paramMemory, paramInstance);
        case 1451:
          return Wat2WasmModuleMachineFuncGroup_0.func_1451(paramInt1, paramMemory, paramInstance);
        case 1453:
          return Wat2WasmModuleMachineFuncGroup_0.func_1453(paramInt1, paramMemory, paramInstance);
        case 1458:
          return Wat2WasmModuleMachineFuncGroup_0.func_1458(paramInt1, paramMemory, paramInstance);
        case 1460:
          return Wat2WasmModuleMachineFuncGroup_0.func_1460(paramInt1, paramMemory, paramInstance);
        case 1462:
          return Wat2WasmModuleMachineFuncGroup_0.func_1462(paramInt1, paramMemory, paramInstance);
        case 1465:
          return Wat2WasmModuleMachineFuncGroup_0.func_1465(paramInt1, paramMemory, paramInstance);
        case 1467:
          return Wat2WasmModuleMachineFuncGroup_0.func_1467(paramInt1, paramMemory, paramInstance);
        case 1469:
          return Wat2WasmModuleMachineFuncGroup_0.func_1469(paramInt1, paramMemory, paramInstance);
        case 1471:
          return Wat2WasmModuleMachineFuncGroup_0.func_1471(paramInt1, paramMemory, paramInstance);
        case 1473:
          return Wat2WasmModuleMachineFuncGroup_0.func_1473(paramInt1, paramMemory, paramInstance);
        case 1475:
          return Wat2WasmModuleMachineFuncGroup_0.func_1475(paramInt1, paramMemory, paramInstance);
        case 1477:
          return Wat2WasmModuleMachineFuncGroup_0.func_1477(paramInt1, paramMemory, paramInstance);
        case 1479:
          return Wat2WasmModuleMachineFuncGroup_0.func_1479(paramInt1, paramMemory, paramInstance);
        case 1481:
          return Wat2WasmModuleMachineFuncGroup_0.func_1481(paramInt1, paramMemory, paramInstance);
        case 1484:
          return Wat2WasmModuleMachineFuncGroup_0.func_1484(paramInt1, paramMemory, paramInstance);
        case 1487:
          return Wat2WasmModuleMachineFuncGroup_0.func_1487(paramInt1, paramMemory, paramInstance);
        case 1493:
          return Wat2WasmModuleMachineFuncGroup_0.func_1493(paramInt1, paramMemory, paramInstance);
        case 1495:
          return Wat2WasmModuleMachineFuncGroup_0.func_1495(paramInt1, paramMemory, paramInstance);
        case 1497:
          return Wat2WasmModuleMachineFuncGroup_0.func_1497(paramInt1, paramMemory, paramInstance);
        case 1499:
          return Wat2WasmModuleMachineFuncGroup_0.func_1499(paramInt1, paramMemory, paramInstance);
        case 1501:
          return Wat2WasmModuleMachineFuncGroup_0.func_1501(paramInt1, paramMemory, paramInstance);
        case 1503:
          return Wat2WasmModuleMachineFuncGroup_0.func_1503(paramInt1, paramMemory, paramInstance);
        case 1505:
          return Wat2WasmModuleMachineFuncGroup_0.func_1505(paramInt1, paramMemory, paramInstance);
        case 1507:
          return Wat2WasmModuleMachineFuncGroup_0.func_1507(paramInt1, paramMemory, paramInstance);
        case 1509:
          return Wat2WasmModuleMachineFuncGroup_0.func_1509(paramInt1, paramMemory, paramInstance);
        case 1511:
          return Wat2WasmModuleMachineFuncGroup_0.func_1511(paramInt1, paramMemory, paramInstance);
        case 1513:
          return Wat2WasmModuleMachineFuncGroup_0.func_1513(paramInt1, paramMemory, paramInstance);
        case 1515:
          return Wat2WasmModuleMachineFuncGroup_0.func_1515(paramInt1, paramMemory, paramInstance);
        case 1517:
          return Wat2WasmModuleMachineFuncGroup_0.func_1517(paramInt1, paramMemory, paramInstance);
        case 1521:
          return Wat2WasmModuleMachineFuncGroup_0.func_1521(paramInt1, paramMemory, paramInstance);
        case 1524:
          return Wat2WasmModuleMachineFuncGroup_0.func_1524(paramInt1, paramMemory, paramInstance);
        case 1526:
          return Wat2WasmModuleMachineFuncGroup_0.func_1526(paramInt1, paramMemory, paramInstance);
        case 1529:
          return Wat2WasmModuleMachineFuncGroup_0.func_1529(paramInt1, paramMemory, paramInstance);
        case 1532:
          return Wat2WasmModuleMachineFuncGroup_0.func_1532(paramInt1, paramMemory, paramInstance);
        case 1535:
          return Wat2WasmModuleMachineFuncGroup_0.func_1535(paramInt1, paramMemory, paramInstance);
        case 1538:
          return Wat2WasmModuleMachineFuncGroup_0.func_1538(paramInt1, paramMemory, paramInstance);
        case 1541:
          return Wat2WasmModuleMachineFuncGroup_0.func_1541(paramInt1, paramMemory, paramInstance);
        case 1545:
          return Wat2WasmModuleMachineFuncGroup_0.func_1545(paramInt1, paramMemory, paramInstance);
        case 1548:
          return Wat2WasmModuleMachineFuncGroup_0.func_1548(paramInt1, paramMemory, paramInstance);
        case 1551:
          return Wat2WasmModuleMachineFuncGroup_0.func_1551(paramInt1, paramMemory, paramInstance);
        case 1553:
          return Wat2WasmModuleMachineFuncGroup_0.func_1553(paramInt1, paramMemory, paramInstance);
        case 1555:
          return Wat2WasmModuleMachineFuncGroup_0.func_1555(paramInt1, paramMemory, paramInstance);
        case 1557:
          return Wat2WasmModuleMachineFuncGroup_0.func_1557(paramInt1, paramMemory, paramInstance);
        case 1559:
          return Wat2WasmModuleMachineFuncGroup_0.func_1559(paramInt1, paramMemory, paramInstance);
        case 1561:
          return Wat2WasmModuleMachineFuncGroup_0.func_1561(paramInt1, paramMemory, paramInstance);
        case 1563:
          return Wat2WasmModuleMachineFuncGroup_0.func_1563(paramInt1, paramMemory, paramInstance);
        case 1565:
          return Wat2WasmModuleMachineFuncGroup_0.func_1565(paramInt1, paramMemory, paramInstance);
        case 1566:
          return Wat2WasmModuleMachineFuncGroup_0.func_1566(paramInt1, paramMemory, paramInstance);
        case 1568:
          return Wat2WasmModuleMachineFuncGroup_0.func_1568(paramInt1, paramMemory, paramInstance);
        case 1569:
          return Wat2WasmModuleMachineFuncGroup_0.func_1569(paramInt1, paramMemory, paramInstance);
        case 1571:
          return Wat2WasmModuleMachineFuncGroup_0.func_1571(paramInt1, paramMemory, paramInstance);
        case 1572:
          return Wat2WasmModuleMachineFuncGroup_0.func_1572(paramInt1, paramMemory, paramInstance);
        case 1579:
          return Wat2WasmModuleMachineFuncGroup_0.func_1579(paramInt1, paramMemory, paramInstance);
        case 1588:
          return Wat2WasmModuleMachineFuncGroup_0.func_1588(paramInt1, paramMemory, paramInstance);
        case 1590:
          return Wat2WasmModuleMachineFuncGroup_0.func_1590(paramInt1, paramMemory, paramInstance);
        case 1593:
          return Wat2WasmModuleMachineFuncGroup_0.func_1593(paramInt1, paramMemory, paramInstance);
        case 1594:
          return Wat2WasmModuleMachineFuncGroup_0.func_1594(paramInt1, paramMemory, paramInstance);
        case 1595:
          return Wat2WasmModuleMachineFuncGroup_0.func_1595(paramInt1, paramMemory, paramInstance);
        case 1599:
          return Wat2WasmModuleMachineFuncGroup_0.func_1599(paramInt1, paramMemory, paramInstance);
        case 1600:
          return Wat2WasmModuleMachineFuncGroup_0.func_1600(paramInt1, paramMemory, paramInstance);
        case 1601:
          return Wat2WasmModuleMachineFuncGroup_0.func_1601(paramInt1, paramMemory, paramInstance);
        case 1604:
          return Wat2WasmModuleMachineFuncGroup_0.func_1604(paramInt1, paramMemory, paramInstance);
        case 1605:
          return Wat2WasmModuleMachineFuncGroup_0.func_1605(paramInt1, paramMemory, paramInstance);
        case 1606:
          return Wat2WasmModuleMachineFuncGroup_0.func_1606(paramInt1, paramMemory, paramInstance);
        case 1611:
          return Wat2WasmModuleMachineFuncGroup_0.func_1611(paramInt1, paramMemory, paramInstance);
        case 1614:
          return Wat2WasmModuleMachineFuncGroup_0.func_1614(paramInt1, paramMemory, paramInstance);
        case 1615:
          return Wat2WasmModuleMachineFuncGroup_0.func_1615(paramInt1, paramMemory, paramInstance);
        case 1624:
          return Wat2WasmModuleMachineFuncGroup_0.func_1624(paramInt1, paramMemory, paramInstance);
        case 1625:
          return Wat2WasmModuleMachineFuncGroup_0.func_1625(paramInt1, paramMemory, paramInstance);
        case 1626:
          return Wat2WasmModuleMachineFuncGroup_0.func_1626(paramInt1, paramMemory, paramInstance);
        case 1627:
          return Wat2WasmModuleMachineFuncGroup_0.func_1627(paramInt1, paramMemory, paramInstance);
        case 1629:
          return Wat2WasmModuleMachineFuncGroup_0.func_1629(paramInt1, paramMemory, paramInstance);
        case 1630:
          return Wat2WasmModuleMachineFuncGroup_0.func_1630(paramInt1, paramMemory, paramInstance);
        case 1633:
          return Wat2WasmModuleMachineFuncGroup_0.func_1633(paramInt1, paramMemory, paramInstance);
        case 1634:
          return Wat2WasmModuleMachineFuncGroup_0.func_1634(paramInt1, paramMemory, paramInstance);
        case 1636:
          return Wat2WasmModuleMachineFuncGroup_0.func_1636(paramInt1, paramMemory, paramInstance);
        case 1637:
          return Wat2WasmModuleMachineFuncGroup_0.func_1637(paramInt1, paramMemory, paramInstance);
        case 1639:
          return Wat2WasmModuleMachineFuncGroup_0.func_1639(paramInt1, paramMemory, paramInstance);
        case 1640:
          return Wat2WasmModuleMachineFuncGroup_0.func_1640(paramInt1, paramMemory, paramInstance);
        case 1641:
          return Wat2WasmModuleMachineFuncGroup_0.func_1641(paramInt1, paramMemory, paramInstance);
        case 1656:
          return Wat2WasmModuleMachineFuncGroup_0.func_1656(paramInt1, paramMemory, paramInstance);
        case 1672:
          return Wat2WasmModuleMachineFuncGroup_0.func_1672(paramInt1, paramMemory, paramInstance);
        case 1673:
          return Wat2WasmModuleMachineFuncGroup_0.func_1673(paramInt1, paramMemory, paramInstance);
        case 1674:
          return Wat2WasmModuleMachineFuncGroup_0.func_1674(paramInt1, paramMemory, paramInstance);
        case 1675:
          return Wat2WasmModuleMachineFuncGroup_0.func_1675(paramInt1, paramMemory, paramInstance);
        case 1676:
          return Wat2WasmModuleMachineFuncGroup_0.func_1676(paramInt1, paramMemory, paramInstance);
        case 1678:
          return Wat2WasmModuleMachineFuncGroup_0.func_1678(paramInt1, paramMemory, paramInstance);
        case 1679:
          return Wat2WasmModuleMachineFuncGroup_0.func_1679(paramInt1, paramMemory, paramInstance);
        case 1681:
          return Wat2WasmModuleMachineFuncGroup_0.func_1681(paramInt1, paramMemory, paramInstance);
        case 1683:
          return Wat2WasmModuleMachineFuncGroup_0.func_1683(paramInt1, paramMemory, paramInstance);
        case 1684:
          return Wat2WasmModuleMachineFuncGroup_0.func_1684(paramInt1, paramMemory, paramInstance);
        case 1690:
          return Wat2WasmModuleMachineFuncGroup_0.func_1690(paramInt1, paramMemory, paramInstance);
        case 1697:
          return Wat2WasmModuleMachineFuncGroup_0.func_1697(paramInt1, paramMemory, paramInstance);
        case 1701:
          return Wat2WasmModuleMachineFuncGroup_0.func_1701(paramInt1, paramMemory, paramInstance);
        case 1703:
          return Wat2WasmModuleMachineFuncGroup_0.func_1703(paramInt1, paramMemory, paramInstance);
        case 1713:
          return Wat2WasmModuleMachineFuncGroup_0.func_1713(paramInt1, paramMemory, paramInstance);
        case 1715:
          return Wat2WasmModuleMachineFuncGroup_0.func_1715(paramInt1, paramMemory, paramInstance);
        case 1716:
          return Wat2WasmModuleMachineFuncGroup_0.func_1716(paramInt1, paramMemory, paramInstance);
        case 1717:
          return Wat2WasmModuleMachineFuncGroup_0.func_1717(paramInt1, paramMemory, paramInstance);
        case 1720:
          return Wat2WasmModuleMachineFuncGroup_0.func_1720(paramInt1, paramMemory, paramInstance);
        case 1721:
          return Wat2WasmModuleMachineFuncGroup_0.func_1721(paramInt1, paramMemory, paramInstance);
        case 1723:
          return Wat2WasmModuleMachineFuncGroup_0.func_1723(paramInt1, paramMemory, paramInstance);
        case 1725:
          return Wat2WasmModuleMachineFuncGroup_0.func_1725(paramInt1, paramMemory, paramInstance);
        case 1740:
          return Wat2WasmModuleMachineFuncGroup_0.func_1740(paramInt1, paramMemory, paramInstance);
        case 1741:
          return Wat2WasmModuleMachineFuncGroup_0.func_1741(paramInt1, paramMemory, paramInstance);
        case 1744:
          return Wat2WasmModuleMachineFuncGroup_0.func_1744(paramInt1, paramMemory, paramInstance);
        case 1746:
          return Wat2WasmModuleMachineFuncGroup_0.func_1746(paramInt1, paramMemory, paramInstance);
        case 1748:
          return Wat2WasmModuleMachineFuncGroup_0.func_1748(paramInt1, paramMemory, paramInstance);
        case 1750:
          return Wat2WasmModuleMachineFuncGroup_0.func_1750(paramInt1, paramMemory, paramInstance);
        case 1755:
          return Wat2WasmModuleMachineFuncGroup_0.func_1755(paramInt1, paramMemory, paramInstance);
        case 1776:
          return Wat2WasmModuleMachineFuncGroup_0.func_1776(paramInt1, paramMemory, paramInstance);
        case 1782:
          return Wat2WasmModuleMachineFuncGroup_0.func_1782(paramInt1, paramMemory, paramInstance);
        case 1783:
          return Wat2WasmModuleMachineFuncGroup_0.func_1783(paramInt1, paramMemory, paramInstance);
        case 1796:
          return Wat2WasmModuleMachineFuncGroup_0.func_1796(paramInt1, paramMemory, paramInstance);
        case 1808:
          return Wat2WasmModuleMachineFuncGroup_0.func_1808(paramInt1, paramMemory, paramInstance);
        case 1811:
          return Wat2WasmModuleMachineFuncGroup_0.func_1811(paramInt1, paramMemory, paramInstance);
        case 1818:
          return Wat2WasmModuleMachineFuncGroup_0.func_1818(paramInt1, paramMemory, paramInstance);
        case 1819:
          return Wat2WasmModuleMachineFuncGroup_0.func_1819(paramInt1, paramMemory, paramInstance);
        case 1822:
          return Wat2WasmModuleMachineFuncGroup_0.func_1822(paramInt1, paramMemory, paramInstance);
        case 1823:
          return Wat2WasmModuleMachineFuncGroup_0.func_1823(paramInt1, paramMemory, paramInstance);
        case 1831:
          return Wat2WasmModuleMachineFuncGroup_0.func_1831(paramInt1, paramMemory, paramInstance);
        case 1832:
          return Wat2WasmModuleMachineFuncGroup_0.func_1832(paramInt1, paramMemory, paramInstance);
        case 1833:
          return Wat2WasmModuleMachineFuncGroup_0.func_1833(paramInt1, paramMemory, paramInstance);
        case 1834:
          return Wat2WasmModuleMachineFuncGroup_0.func_1834(paramInt1, paramMemory, paramInstance);
        case 1835:
          return Wat2WasmModuleMachineFuncGroup_0.func_1835(paramInt1, paramMemory, paramInstance);
        case 1844:
          return Wat2WasmModuleMachineFuncGroup_0.func_1844(paramInt1, paramMemory, paramInstance);
        case 1850:
          return Wat2WasmModuleMachineFuncGroup_0.func_1850(paramInt1, paramMemory, paramInstance);
        case 1853:
          return Wat2WasmModuleMachineFuncGroup_0.func_1853(paramInt1, paramMemory, paramInstance);
        case 1854:
          return Wat2WasmModuleMachineFuncGroup_0.func_1854(paramInt1, paramMemory, paramInstance);
        case 1859:
          return Wat2WasmModuleMachineFuncGroup_0.func_1859(paramInt1, paramMemory, paramInstance);
        case 1864:
          return Wat2WasmModuleMachineFuncGroup_0.func_1864(paramInt1, paramMemory, paramInstance);
        case 1877:
          return Wat2WasmModuleMachineFuncGroup_0.func_1877(paramInt1, paramMemory, paramInstance);
        case 1878:
          return Wat2WasmModuleMachineFuncGroup_0.func_1878(paramInt1, paramMemory, paramInstance);
        case 1880:
          return Wat2WasmModuleMachineFuncGroup_0.func_1880(paramInt1, paramMemory, paramInstance);
        case 1896:
          return Wat2WasmModuleMachineFuncGroup_0.func_1896(paramInt1, paramMemory, paramInstance);
        case 1897:
          return Wat2WasmModuleMachineFuncGroup_0.func_1897(paramInt1, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1 }, 1, i, instance)[0];
  }
  
  public static void call_indirect_2(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt5);
    int i = tableInstance.requiredRef(paramInt4);
    Instance instance = tableInstance.instance(paramInt4);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 37:
          Wat2WasmModuleMachineFuncGroup_0.func_37(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 39:
          Wat2WasmModuleMachineFuncGroup_0.func_39(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 51:
          Wat2WasmModuleMachineFuncGroup_0.func_51(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 53:
          Wat2WasmModuleMachineFuncGroup_0.func_53(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 55:
          Wat2WasmModuleMachineFuncGroup_0.func_55(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 64:
          Wat2WasmModuleMachineFuncGroup_0.func_64(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 115:
          Wat2WasmModuleMachineFuncGroup_0.func_115(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 117:
          Wat2WasmModuleMachineFuncGroup_0.func_117(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 152:
          Wat2WasmModuleMachineFuncGroup_0.func_152(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 169:
          Wat2WasmModuleMachineFuncGroup_0.func_169(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 170:
          Wat2WasmModuleMachineFuncGroup_0.func_170(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 173:
          Wat2WasmModuleMachineFuncGroup_0.func_173(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 176:
          Wat2WasmModuleMachineFuncGroup_0.func_176(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 205:
          Wat2WasmModuleMachineFuncGroup_0.func_205(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 211:
          Wat2WasmModuleMachineFuncGroup_0.func_211(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 212:
          Wat2WasmModuleMachineFuncGroup_0.func_212(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 223:
          Wat2WasmModuleMachineFuncGroup_0.func_223(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 246:
          Wat2WasmModuleMachineFuncGroup_0.func_246(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 254:
          Wat2WasmModuleMachineFuncGroup_0.func_254(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 344:
          Wat2WasmModuleMachineFuncGroup_0.func_344(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 545:
          Wat2WasmModuleMachineFuncGroup_0.func_545(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 561:
          Wat2WasmModuleMachineFuncGroup_0.func_561(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 567:
          Wat2WasmModuleMachineFuncGroup_0.func_567(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 573:
          Wat2WasmModuleMachineFuncGroup_0.func_573(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 802:
          Wat2WasmModuleMachineFuncGroup_0.func_802(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 853:
          Wat2WasmModuleMachineFuncGroup_0.func_853(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 862:
          Wat2WasmModuleMachineFuncGroup_0.func_862(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 900:
          Wat2WasmModuleMachineFuncGroup_0.func_900(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 925:
          Wat2WasmModuleMachineFuncGroup_0.func_925(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1023:
          Wat2WasmModuleMachineFuncGroup_0.func_1023(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1027:
          Wat2WasmModuleMachineFuncGroup_0.func_1027(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1099:
          Wat2WasmModuleMachineFuncGroup_0.func_1099(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1101:
          Wat2WasmModuleMachineFuncGroup_0.func_1101(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1215:
          Wat2WasmModuleMachineFuncGroup_0.func_1215(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1234:
          Wat2WasmModuleMachineFuncGroup_0.func_1234(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1304:
          Wat2WasmModuleMachineFuncGroup_0.func_1304(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1320:
          Wat2WasmModuleMachineFuncGroup_0.func_1320(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1616:
          Wat2WasmModuleMachineFuncGroup_0.func_1616(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1620:
          Wat2WasmModuleMachineFuncGroup_0.func_1620(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1642:
          Wat2WasmModuleMachineFuncGroup_0.func_1642(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1646:
          Wat2WasmModuleMachineFuncGroup_0.func_1646(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1647:
          Wat2WasmModuleMachineFuncGroup_0.func_1647(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1689:
          Wat2WasmModuleMachineFuncGroup_0.func_1689(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1694:
          Wat2WasmModuleMachineFuncGroup_0.func_1694(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1695:
          Wat2WasmModuleMachineFuncGroup_0.func_1695(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1704:
          Wat2WasmModuleMachineFuncGroup_0.func_1704(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1708:
          Wat2WasmModuleMachineFuncGroup_0.func_1708(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1709:
          Wat2WasmModuleMachineFuncGroup_0.func_1709(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1719:
          Wat2WasmModuleMachineFuncGroup_0.func_1719(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
        case 1872:
          Wat2WasmModuleMachineFuncGroup_0.func_1872(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[3])[0] = paramInt1;
    (new long[3])[1] = paramInt2;
    (new long[3])[2] = paramInt3;
  }
  
  public static void call_indirect_3(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt4);
    int i = tableInstance.requiredRef(paramInt3);
    Instance instance = tableInstance.instance(paramInt3);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 25:
          Wat2WasmModuleMachineFuncGroup_0.func_25(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 30:
          Wat2WasmModuleMachineFuncGroup_0.func_30(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 34:
          Wat2WasmModuleMachineFuncGroup_0.func_34(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 36:
          Wat2WasmModuleMachineFuncGroup_0.func_36(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 38:
          Wat2WasmModuleMachineFuncGroup_0.func_38(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 42:
          Wat2WasmModuleMachineFuncGroup_0.func_42(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 43:
          Wat2WasmModuleMachineFuncGroup_0.func_43(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 47:
          Wat2WasmModuleMachineFuncGroup_0.func_47(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 49:
          Wat2WasmModuleMachineFuncGroup_0.func_49(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 50:
          Wat2WasmModuleMachineFuncGroup_0.func_50(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 52:
          Wat2WasmModuleMachineFuncGroup_0.func_52(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 56:
          Wat2WasmModuleMachineFuncGroup_0.func_56(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 57:
          Wat2WasmModuleMachineFuncGroup_0.func_57(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 58:
          Wat2WasmModuleMachineFuncGroup_0.func_58(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 59:
          Wat2WasmModuleMachineFuncGroup_0.func_59(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 60:
          Wat2WasmModuleMachineFuncGroup_0.func_60(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 61:
          Wat2WasmModuleMachineFuncGroup_0.func_61(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 62:
          Wat2WasmModuleMachineFuncGroup_0.func_62(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 65:
          Wat2WasmModuleMachineFuncGroup_0.func_65(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 66:
          Wat2WasmModuleMachineFuncGroup_0.func_66(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 67:
          Wat2WasmModuleMachineFuncGroup_0.func_67(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 68:
          Wat2WasmModuleMachineFuncGroup_0.func_68(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 69:
          Wat2WasmModuleMachineFuncGroup_0.func_69(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 70:
          Wat2WasmModuleMachineFuncGroup_0.func_70(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 72:
          Wat2WasmModuleMachineFuncGroup_0.func_72(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 84:
          Wat2WasmModuleMachineFuncGroup_0.func_84(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 89:
          Wat2WasmModuleMachineFuncGroup_0.func_89(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 110:
          Wat2WasmModuleMachineFuncGroup_0.func_110(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 111:
          Wat2WasmModuleMachineFuncGroup_0.func_111(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 112:
          Wat2WasmModuleMachineFuncGroup_0.func_112(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 113:
          Wat2WasmModuleMachineFuncGroup_0.func_113(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 129:
          Wat2WasmModuleMachineFuncGroup_0.func_129(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 136:
          Wat2WasmModuleMachineFuncGroup_0.func_136(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 139:
          Wat2WasmModuleMachineFuncGroup_0.func_139(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 140:
          Wat2WasmModuleMachineFuncGroup_0.func_140(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 141:
          Wat2WasmModuleMachineFuncGroup_0.func_141(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 142:
          Wat2WasmModuleMachineFuncGroup_0.func_142(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 143:
          Wat2WasmModuleMachineFuncGroup_0.func_143(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 144:
          Wat2WasmModuleMachineFuncGroup_0.func_144(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 145:
          Wat2WasmModuleMachineFuncGroup_0.func_145(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 147:
          Wat2WasmModuleMachineFuncGroup_0.func_147(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 148:
          Wat2WasmModuleMachineFuncGroup_0.func_148(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 149:
          Wat2WasmModuleMachineFuncGroup_0.func_149(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 150:
          Wat2WasmModuleMachineFuncGroup_0.func_150(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 151:
          Wat2WasmModuleMachineFuncGroup_0.func_151(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 153:
          Wat2WasmModuleMachineFuncGroup_0.func_153(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 161:
          Wat2WasmModuleMachineFuncGroup_0.func_161(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 166:
          Wat2WasmModuleMachineFuncGroup_0.func_166(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 191:
          Wat2WasmModuleMachineFuncGroup_0.func_191(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 192:
          Wat2WasmModuleMachineFuncGroup_0.func_192(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 196:
          Wat2WasmModuleMachineFuncGroup_0.func_196(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 199:
          Wat2WasmModuleMachineFuncGroup_0.func_199(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 203:
          Wat2WasmModuleMachineFuncGroup_0.func_203(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 204:
          Wat2WasmModuleMachineFuncGroup_0.func_204(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 209:
          Wat2WasmModuleMachineFuncGroup_0.func_209(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 214:
          Wat2WasmModuleMachineFuncGroup_0.func_214(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 220:
          Wat2WasmModuleMachineFuncGroup_0.func_220(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 226:
          Wat2WasmModuleMachineFuncGroup_0.func_226(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 248:
          Wat2WasmModuleMachineFuncGroup_0.func_248(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 252:
          Wat2WasmModuleMachineFuncGroup_0.func_252(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 336:
          Wat2WasmModuleMachineFuncGroup_0.func_336(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 343:
          Wat2WasmModuleMachineFuncGroup_0.func_343(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 351:
          Wat2WasmModuleMachineFuncGroup_0.func_351(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 362:
          Wat2WasmModuleMachineFuncGroup_0.func_362(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 367:
          Wat2WasmModuleMachineFuncGroup_0.func_367(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 417:
          Wat2WasmModuleMachineFuncGroup_0.func_417(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 449:
          Wat2WasmModuleMachineFuncGroup_0.func_449(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 534:
          Wat2WasmModuleMachineFuncGroup_0.func_534(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 535:
          Wat2WasmModuleMachineFuncGroup_0.func_535(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 536:
          Wat2WasmModuleMachineFuncGroup_0.func_536(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 537:
          Wat2WasmModuleMachineFuncGroup_0.func_537(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 544:
          Wat2WasmModuleMachineFuncGroup_0.func_544(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 557:
          Wat2WasmModuleMachineFuncGroup_0.func_557(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 558:
          Wat2WasmModuleMachineFuncGroup_0.func_558(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 559:
          Wat2WasmModuleMachineFuncGroup_0.func_559(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 560:
          Wat2WasmModuleMachineFuncGroup_0.func_560(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 562:
          Wat2WasmModuleMachineFuncGroup_0.func_562(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 563:
          Wat2WasmModuleMachineFuncGroup_0.func_563(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 565:
          Wat2WasmModuleMachineFuncGroup_0.func_565(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 574:
          Wat2WasmModuleMachineFuncGroup_0.func_574(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 576:
          Wat2WasmModuleMachineFuncGroup_0.func_576(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 809:
          Wat2WasmModuleMachineFuncGroup_0.func_809(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 816:
          Wat2WasmModuleMachineFuncGroup_0.func_816(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 820:
          Wat2WasmModuleMachineFuncGroup_0.func_820(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 822:
          Wat2WasmModuleMachineFuncGroup_0.func_822(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 830:
          Wat2WasmModuleMachineFuncGroup_0.func_830(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 833:
          Wat2WasmModuleMachineFuncGroup_0.func_833(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 834:
          Wat2WasmModuleMachineFuncGroup_0.func_834(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 835:
          Wat2WasmModuleMachineFuncGroup_0.func_835(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 836:
          Wat2WasmModuleMachineFuncGroup_0.func_836(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 837:
          Wat2WasmModuleMachineFuncGroup_0.func_837(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 843:
          Wat2WasmModuleMachineFuncGroup_0.func_843(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 844:
          Wat2WasmModuleMachineFuncGroup_0.func_844(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 855:
          Wat2WasmModuleMachineFuncGroup_0.func_855(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 929:
          Wat2WasmModuleMachineFuncGroup_0.func_929(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 984:
          Wat2WasmModuleMachineFuncGroup_0.func_984(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1003:
          Wat2WasmModuleMachineFuncGroup_0.func_1003(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1058:
          Wat2WasmModuleMachineFuncGroup_0.func_1058(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1094:
          Wat2WasmModuleMachineFuncGroup_0.func_1094(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1095:
          Wat2WasmModuleMachineFuncGroup_0.func_1095(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1096:
          Wat2WasmModuleMachineFuncGroup_0.func_1096(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1105:
          Wat2WasmModuleMachineFuncGroup_0.func_1105(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1108:
          Wat2WasmModuleMachineFuncGroup_0.func_1108(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1153:
          Wat2WasmModuleMachineFuncGroup_0.func_1153(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1162:
          Wat2WasmModuleMachineFuncGroup_0.func_1162(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1185:
          Wat2WasmModuleMachineFuncGroup_0.func_1185(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1188:
          Wat2WasmModuleMachineFuncGroup_0.func_1188(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1189:
          Wat2WasmModuleMachineFuncGroup_0.func_1189(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1210:
          Wat2WasmModuleMachineFuncGroup_0.func_1210(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1218:
          Wat2WasmModuleMachineFuncGroup_0.func_1218(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1231:
          Wat2WasmModuleMachineFuncGroup_0.func_1231(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1252:
          Wat2WasmModuleMachineFuncGroup_0.func_1252(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1283:
          Wat2WasmModuleMachineFuncGroup_0.func_1283(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1303:
          Wat2WasmModuleMachineFuncGroup_0.func_1303(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1306:
          Wat2WasmModuleMachineFuncGroup_0.func_1306(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1409:
          Wat2WasmModuleMachineFuncGroup_0.func_1409(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1413:
          Wat2WasmModuleMachineFuncGroup_0.func_1413(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1580:
          Wat2WasmModuleMachineFuncGroup_0.func_1580(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1584:
          Wat2WasmModuleMachineFuncGroup_0.func_1584(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1589:
          Wat2WasmModuleMachineFuncGroup_0.func_1589(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1608:
          Wat2WasmModuleMachineFuncGroup_0.func_1608(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1609:
          Wat2WasmModuleMachineFuncGroup_0.func_1609(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1610:
          Wat2WasmModuleMachineFuncGroup_0.func_1610(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1617:
          Wat2WasmModuleMachineFuncGroup_0.func_1617(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1621:
          Wat2WasmModuleMachineFuncGroup_0.func_1621(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1622:
          Wat2WasmModuleMachineFuncGroup_0.func_1622(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1623:
          Wat2WasmModuleMachineFuncGroup_0.func_1623(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1628:
          Wat2WasmModuleMachineFuncGroup_0.func_1628(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1660:
          Wat2WasmModuleMachineFuncGroup_0.func_1660(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1664:
          Wat2WasmModuleMachineFuncGroup_0.func_1664(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1665:
          Wat2WasmModuleMachineFuncGroup_0.func_1665(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1668:
          Wat2WasmModuleMachineFuncGroup_0.func_1668(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1669:
          Wat2WasmModuleMachineFuncGroup_0.func_1669(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1710:
          Wat2WasmModuleMachineFuncGroup_0.func_1710(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1711:
          Wat2WasmModuleMachineFuncGroup_0.func_1711(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1780:
          Wat2WasmModuleMachineFuncGroup_0.func_1780(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
        case 1787:
          Wat2WasmModuleMachineFuncGroup_0.func_1787(paramInt1, paramInt2, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[2])[0] = paramInt1;
    (new long[2])[1] = paramInt2;
  }
  
  public static int call_indirect_4(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt6);
    int i = tableInstance.requiredRef(paramInt5);
    Instance instance = tableInstance.instance(paramInt5);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 9:
          return Wat2WasmModuleMachineFuncGroup_0.func_9(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 11:
          return Wat2WasmModuleMachineFuncGroup_0.func_11(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 48:
          return Wat2WasmModuleMachineFuncGroup_0.func_48(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 77:
          return Wat2WasmModuleMachineFuncGroup_0.func_77(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 168:
          return Wat2WasmModuleMachineFuncGroup_0.func_168(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 171:
          return Wat2WasmModuleMachineFuncGroup_0.func_171(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 182:
          return Wat2WasmModuleMachineFuncGroup_0.func_182(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 197:
          return Wat2WasmModuleMachineFuncGroup_0.func_197(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 231:
          return Wat2WasmModuleMachineFuncGroup_0.func_231(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 232:
          return Wat2WasmModuleMachineFuncGroup_0.func_232(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 237:
          return Wat2WasmModuleMachineFuncGroup_0.func_237(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 238:
          return Wat2WasmModuleMachineFuncGroup_0.func_238(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 256:
          return Wat2WasmModuleMachineFuncGroup_0.func_256(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 258:
          return Wat2WasmModuleMachineFuncGroup_0.func_258(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 277:
          return Wat2WasmModuleMachineFuncGroup_0.func_277(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 288:
          return Wat2WasmModuleMachineFuncGroup_0.func_288(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 337:
          return Wat2WasmModuleMachineFuncGroup_0.func_337(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 339:
          return Wat2WasmModuleMachineFuncGroup_0.func_339(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 345:
          return Wat2WasmModuleMachineFuncGroup_0.func_345(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 346:
          return Wat2WasmModuleMachineFuncGroup_0.func_346(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 347:
          return Wat2WasmModuleMachineFuncGroup_0.func_347(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 348:
          return Wat2WasmModuleMachineFuncGroup_0.func_348(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 360:
          return Wat2WasmModuleMachineFuncGroup_0.func_360(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 363:
          return Wat2WasmModuleMachineFuncGroup_0.func_363(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 372:
          return Wat2WasmModuleMachineFuncGroup_0.func_372(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 388:
          return Wat2WasmModuleMachineFuncGroup_0.func_388(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 390:
          return Wat2WasmModuleMachineFuncGroup_0.func_390(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 410:
          return Wat2WasmModuleMachineFuncGroup_0.func_410(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 413:
          return Wat2WasmModuleMachineFuncGroup_0.func_413(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 422:
          return Wat2WasmModuleMachineFuncGroup_0.func_422(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 424:
          return Wat2WasmModuleMachineFuncGroup_0.func_424(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 428:
          return Wat2WasmModuleMachineFuncGroup_0.func_428(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 430:
          return Wat2WasmModuleMachineFuncGroup_0.func_430(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 434:
          return Wat2WasmModuleMachineFuncGroup_0.func_434(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 540:
          return Wat2WasmModuleMachineFuncGroup_0.func_540(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 541:
          return Wat2WasmModuleMachineFuncGroup_0.func_541(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 542:
          return Wat2WasmModuleMachineFuncGroup_0.func_542(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 543:
          return Wat2WasmModuleMachineFuncGroup_0.func_543(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 548:
          return Wat2WasmModuleMachineFuncGroup_0.func_548(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 549:
          return Wat2WasmModuleMachineFuncGroup_0.func_549(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 550:
          return Wat2WasmModuleMachineFuncGroup_0.func_550(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 551:
          return Wat2WasmModuleMachineFuncGroup_0.func_551(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 552:
          return Wat2WasmModuleMachineFuncGroup_0.func_552(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 554:
          return Wat2WasmModuleMachineFuncGroup_0.func_554(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 556:
          return Wat2WasmModuleMachineFuncGroup_0.func_556(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 578:
          return Wat2WasmModuleMachineFuncGroup_0.func_578(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 579:
          return Wat2WasmModuleMachineFuncGroup_0.func_579(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 581:
          return Wat2WasmModuleMachineFuncGroup_0.func_581(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 589:
          return Wat2WasmModuleMachineFuncGroup_0.func_589(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 590:
          return Wat2WasmModuleMachineFuncGroup_0.func_590(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 591:
          return Wat2WasmModuleMachineFuncGroup_0.func_591(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 594:
          return Wat2WasmModuleMachineFuncGroup_0.func_594(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 598:
          return Wat2WasmModuleMachineFuncGroup_0.func_598(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 611:
          return Wat2WasmModuleMachineFuncGroup_0.func_611(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 614:
          return Wat2WasmModuleMachineFuncGroup_0.func_614(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 616:
          return Wat2WasmModuleMachineFuncGroup_0.func_616(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 618:
          return Wat2WasmModuleMachineFuncGroup_0.func_618(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 619:
          return Wat2WasmModuleMachineFuncGroup_0.func_619(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 620:
          return Wat2WasmModuleMachineFuncGroup_0.func_620(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 621:
          return Wat2WasmModuleMachineFuncGroup_0.func_621(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 625:
          return Wat2WasmModuleMachineFuncGroup_0.func_625(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 632:
          return Wat2WasmModuleMachineFuncGroup_0.func_632(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 637:
          return Wat2WasmModuleMachineFuncGroup_0.func_637(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 641:
          return Wat2WasmModuleMachineFuncGroup_0.func_641(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 746:
          return Wat2WasmModuleMachineFuncGroup_0.func_746(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 791:
          return Wat2WasmModuleMachineFuncGroup_0.func_791(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 801:
          return Wat2WasmModuleMachineFuncGroup_0.func_801(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 847:
          return Wat2WasmModuleMachineFuncGroup_0.func_847(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 848:
          return Wat2WasmModuleMachineFuncGroup_0.func_848(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 854:
          return Wat2WasmModuleMachineFuncGroup_0.func_854(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 874:
          return Wat2WasmModuleMachineFuncGroup_0.func_874(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 878:
          return Wat2WasmModuleMachineFuncGroup_0.func_878(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 882:
          return Wat2WasmModuleMachineFuncGroup_0.func_882(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 884:
          return Wat2WasmModuleMachineFuncGroup_0.func_884(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 899:
          return Wat2WasmModuleMachineFuncGroup_0.func_899(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 908:
          return Wat2WasmModuleMachineFuncGroup_0.func_908(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 928:
          return Wat2WasmModuleMachineFuncGroup_0.func_928(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 998:
          return Wat2WasmModuleMachineFuncGroup_0.func_998(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1011:
          return Wat2WasmModuleMachineFuncGroup_0.func_1011(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1021:
          return Wat2WasmModuleMachineFuncGroup_0.func_1021(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1024:
          return Wat2WasmModuleMachineFuncGroup_0.func_1024(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1028:
          return Wat2WasmModuleMachineFuncGroup_0.func_1028(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1031:
          return Wat2WasmModuleMachineFuncGroup_0.func_1031(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1033:
          return Wat2WasmModuleMachineFuncGroup_0.func_1033(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1035:
          return Wat2WasmModuleMachineFuncGroup_0.func_1035(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1047:
          return Wat2WasmModuleMachineFuncGroup_0.func_1047(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1057:
          return Wat2WasmModuleMachineFuncGroup_0.func_1057(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1065:
          return Wat2WasmModuleMachineFuncGroup_0.func_1065(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1073:
          return Wat2WasmModuleMachineFuncGroup_0.func_1073(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1216:
          return Wat2WasmModuleMachineFuncGroup_0.func_1216(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1238:
          return Wat2WasmModuleMachineFuncGroup_0.func_1238(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1272:
          return Wat2WasmModuleMachineFuncGroup_0.func_1272(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1273:
          return Wat2WasmModuleMachineFuncGroup_0.func_1273(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1281:
          return Wat2WasmModuleMachineFuncGroup_0.func_1281(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1284:
          return Wat2WasmModuleMachineFuncGroup_0.func_1284(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1288:
          return Wat2WasmModuleMachineFuncGroup_0.func_1288(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1289:
          return Wat2WasmModuleMachineFuncGroup_0.func_1289(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1290:
          return Wat2WasmModuleMachineFuncGroup_0.func_1290(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1291:
          return Wat2WasmModuleMachineFuncGroup_0.func_1291(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1292:
          return Wat2WasmModuleMachineFuncGroup_0.func_1292(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1293:
          return Wat2WasmModuleMachineFuncGroup_0.func_1293(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1295:
          return Wat2WasmModuleMachineFuncGroup_0.func_1295(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1296:
          return Wat2WasmModuleMachineFuncGroup_0.func_1296(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1301:
          return Wat2WasmModuleMachineFuncGroup_0.func_1301(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1307:
          return Wat2WasmModuleMachineFuncGroup_0.func_1307(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1648:
          return Wat2WasmModuleMachineFuncGroup_0.func_1648(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1757:
          return Wat2WasmModuleMachineFuncGroup_0.func_1757(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1801:
          return Wat2WasmModuleMachineFuncGroup_0.func_1801(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1803:
          return Wat2WasmModuleMachineFuncGroup_0.func_1803(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1804:
          return Wat2WasmModuleMachineFuncGroup_0.func_1804(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1810:
          return Wat2WasmModuleMachineFuncGroup_0.func_1810(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1815:
          return Wat2WasmModuleMachineFuncGroup_0.func_1815(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1851:
          return Wat2WasmModuleMachineFuncGroup_0.func_1851(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1856:
          return Wat2WasmModuleMachineFuncGroup_0.func_1856(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1865:
          return Wat2WasmModuleMachineFuncGroup_0.func_1865(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
        case 1875:
          return Wat2WasmModuleMachineFuncGroup_0.func_1875(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4 }, 4, i, instance)[0];
  }
  
  public static int call_indirect_5(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt4);
    int i = tableInstance.requiredRef(paramInt3);
    Instance instance = tableInstance.instance(paramInt3);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 0:
          return Wat2WasmModuleMachineFuncGroup_0.func_0(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1:
          return Wat2WasmModuleMachineFuncGroup_0.func_1(paramInt1, paramInt2, paramMemory, paramInstance);
        case 2:
          return Wat2WasmModuleMachineFuncGroup_0.func_2(paramInt1, paramInt2, paramMemory, paramInstance);
        case 3:
          return Wat2WasmModuleMachineFuncGroup_0.func_3(paramInt1, paramInt2, paramMemory, paramInstance);
        case 5:
          return Wat2WasmModuleMachineFuncGroup_0.func_5(paramInt1, paramInt2, paramMemory, paramInstance);
        case 6:
          return Wat2WasmModuleMachineFuncGroup_0.func_6(paramInt1, paramInt2, paramMemory, paramInstance);
        case 7:
          return Wat2WasmModuleMachineFuncGroup_0.func_7(paramInt1, paramInt2, paramMemory, paramInstance);
        case 21:
          return Wat2WasmModuleMachineFuncGroup_0.func_21(paramInt1, paramInt2, paramMemory, paramInstance);
        case 33:
          return Wat2WasmModuleMachineFuncGroup_0.func_33(paramInt1, paramInt2, paramMemory, paramInstance);
        case 44:
          return Wat2WasmModuleMachineFuncGroup_0.func_44(paramInt1, paramInt2, paramMemory, paramInstance);
        case 71:
          return Wat2WasmModuleMachineFuncGroup_0.func_71(paramInt1, paramInt2, paramMemory, paramInstance);
        case 82:
          return Wat2WasmModuleMachineFuncGroup_0.func_82(paramInt1, paramInt2, paramMemory, paramInstance);
        case 116:
          return Wat2WasmModuleMachineFuncGroup_0.func_116(paramInt1, paramInt2, paramMemory, paramInstance);
        case 119:
          return Wat2WasmModuleMachineFuncGroup_0.func_119(paramInt1, paramInt2, paramMemory, paramInstance);
        case 120:
          return Wat2WasmModuleMachineFuncGroup_0.func_120(paramInt1, paramInt2, paramMemory, paramInstance);
        case 121:
          return Wat2WasmModuleMachineFuncGroup_0.func_121(paramInt1, paramInt2, paramMemory, paramInstance);
        case 122:
          return Wat2WasmModuleMachineFuncGroup_0.func_122(paramInt1, paramInt2, paramMemory, paramInstance);
        case 123:
          return Wat2WasmModuleMachineFuncGroup_0.func_123(paramInt1, paramInt2, paramMemory, paramInstance);
        case 124:
          return Wat2WasmModuleMachineFuncGroup_0.func_124(paramInt1, paramInt2, paramMemory, paramInstance);
        case 125:
          return Wat2WasmModuleMachineFuncGroup_0.func_125(paramInt1, paramInt2, paramMemory, paramInstance);
        case 126:
          return Wat2WasmModuleMachineFuncGroup_0.func_126(paramInt1, paramInt2, paramMemory, paramInstance);
        case 127:
          return Wat2WasmModuleMachineFuncGroup_0.func_127(paramInt1, paramInt2, paramMemory, paramInstance);
        case 128:
          return Wat2WasmModuleMachineFuncGroup_0.func_128(paramInt1, paramInt2, paramMemory, paramInstance);
        case 131:
          return Wat2WasmModuleMachineFuncGroup_0.func_131(paramInt1, paramInt2, paramMemory, paramInstance);
        case 132:
          return Wat2WasmModuleMachineFuncGroup_0.func_132(paramInt1, paramInt2, paramMemory, paramInstance);
        case 133:
          return Wat2WasmModuleMachineFuncGroup_0.func_133(paramInt1, paramInt2, paramMemory, paramInstance);
        case 134:
          return Wat2WasmModuleMachineFuncGroup_0.func_134(paramInt1, paramInt2, paramMemory, paramInstance);
        case 135:
          return Wat2WasmModuleMachineFuncGroup_0.func_135(paramInt1, paramInt2, paramMemory, paramInstance);
        case 138:
          return Wat2WasmModuleMachineFuncGroup_0.func_138(paramInt1, paramInt2, paramMemory, paramInstance);
        case 157:
          return Wat2WasmModuleMachineFuncGroup_0.func_157(paramInt1, paramInt2, paramMemory, paramInstance);
        case 158:
          return Wat2WasmModuleMachineFuncGroup_0.func_158(paramInt1, paramInt2, paramMemory, paramInstance);
        case 159:
          return Wat2WasmModuleMachineFuncGroup_0.func_159(paramInt1, paramInt2, paramMemory, paramInstance);
        case 160:
          return Wat2WasmModuleMachineFuncGroup_0.func_160(paramInt1, paramInt2, paramMemory, paramInstance);
        case 163:
          return Wat2WasmModuleMachineFuncGroup_0.func_163(paramInt1, paramInt2, paramMemory, paramInstance);
        case 164:
          return Wat2WasmModuleMachineFuncGroup_0.func_164(paramInt1, paramInt2, paramMemory, paramInstance);
        case 181:
          return Wat2WasmModuleMachineFuncGroup_0.func_181(paramInt1, paramInt2, paramMemory, paramInstance);
        case 187:
          return Wat2WasmModuleMachineFuncGroup_0.func_187(paramInt1, paramInt2, paramMemory, paramInstance);
        case 189:
          return Wat2WasmModuleMachineFuncGroup_0.func_189(paramInt1, paramInt2, paramMemory, paramInstance);
        case 194:
          return Wat2WasmModuleMachineFuncGroup_0.func_194(paramInt1, paramInt2, paramMemory, paramInstance);
        case 227:
          return Wat2WasmModuleMachineFuncGroup_0.func_227(paramInt1, paramInt2, paramMemory, paramInstance);
        case 229:
          return Wat2WasmModuleMachineFuncGroup_0.func_229(paramInt1, paramInt2, paramMemory, paramInstance);
        case 233:
          return Wat2WasmModuleMachineFuncGroup_0.func_233(paramInt1, paramInt2, paramMemory, paramInstance);
        case 239:
          return Wat2WasmModuleMachineFuncGroup_0.func_239(paramInt1, paramInt2, paramMemory, paramInstance);
        case 250:
          return Wat2WasmModuleMachineFuncGroup_0.func_250(paramInt1, paramInt2, paramMemory, paramInstance);
        case 251:
          return Wat2WasmModuleMachineFuncGroup_0.func_251(paramInt1, paramInt2, paramMemory, paramInstance);
        case 261:
          return Wat2WasmModuleMachineFuncGroup_0.func_261(paramInt1, paramInt2, paramMemory, paramInstance);
        case 267:
          return Wat2WasmModuleMachineFuncGroup_0.func_267(paramInt1, paramInt2, paramMemory, paramInstance);
        case 269:
          return Wat2WasmModuleMachineFuncGroup_0.func_269(paramInt1, paramInt2, paramMemory, paramInstance);
        case 271:
          return Wat2WasmModuleMachineFuncGroup_0.func_271(paramInt1, paramInt2, paramMemory, paramInstance);
        case 272:
          return Wat2WasmModuleMachineFuncGroup_0.func_272(paramInt1, paramInt2, paramMemory, paramInstance);
        case 274:
          return Wat2WasmModuleMachineFuncGroup_0.func_274(paramInt1, paramInt2, paramMemory, paramInstance);
        case 278:
          return Wat2WasmModuleMachineFuncGroup_0.func_278(paramInt1, paramInt2, paramMemory, paramInstance);
        case 281:
          return Wat2WasmModuleMachineFuncGroup_0.func_281(paramInt1, paramInt2, paramMemory, paramInstance);
        case 282:
          return Wat2WasmModuleMachineFuncGroup_0.func_282(paramInt1, paramInt2, paramMemory, paramInstance);
        case 283:
          return Wat2WasmModuleMachineFuncGroup_0.func_283(paramInt1, paramInt2, paramMemory, paramInstance);
        case 284:
          return Wat2WasmModuleMachineFuncGroup_0.func_284(paramInt1, paramInt2, paramMemory, paramInstance);
        case 285:
          return Wat2WasmModuleMachineFuncGroup_0.func_285(paramInt1, paramInt2, paramMemory, paramInstance);
        case 291:
          return Wat2WasmModuleMachineFuncGroup_0.func_291(paramInt1, paramInt2, paramMemory, paramInstance);
        case 292:
          return Wat2WasmModuleMachineFuncGroup_0.func_292(paramInt1, paramInt2, paramMemory, paramInstance);
        case 294:
          return Wat2WasmModuleMachineFuncGroup_0.func_294(paramInt1, paramInt2, paramMemory, paramInstance);
        case 295:
          return Wat2WasmModuleMachineFuncGroup_0.func_295(paramInt1, paramInt2, paramMemory, paramInstance);
        case 296:
          return Wat2WasmModuleMachineFuncGroup_0.func_296(paramInt1, paramInt2, paramMemory, paramInstance);
        case 299:
          return Wat2WasmModuleMachineFuncGroup_0.func_299(paramInt1, paramInt2, paramMemory, paramInstance);
        case 300:
          return Wat2WasmModuleMachineFuncGroup_0.func_300(paramInt1, paramInt2, paramMemory, paramInstance);
        case 301:
          return Wat2WasmModuleMachineFuncGroup_0.func_301(paramInt1, paramInt2, paramMemory, paramInstance);
        case 303:
          return Wat2WasmModuleMachineFuncGroup_0.func_303(paramInt1, paramInt2, paramMemory, paramInstance);
        case 305:
          return Wat2WasmModuleMachineFuncGroup_0.func_305(paramInt1, paramInt2, paramMemory, paramInstance);
        case 310:
          return Wat2WasmModuleMachineFuncGroup_0.func_310(paramInt1, paramInt2, paramMemory, paramInstance);
        case 313:
          return Wat2WasmModuleMachineFuncGroup_0.func_313(paramInt1, paramInt2, paramMemory, paramInstance);
        case 315:
          return Wat2WasmModuleMachineFuncGroup_0.func_315(paramInt1, paramInt2, paramMemory, paramInstance);
        case 316:
          return Wat2WasmModuleMachineFuncGroup_0.func_316(paramInt1, paramInt2, paramMemory, paramInstance);
        case 319:
          return Wat2WasmModuleMachineFuncGroup_0.func_319(paramInt1, paramInt2, paramMemory, paramInstance);
        case 322:
          return Wat2WasmModuleMachineFuncGroup_0.func_322(paramInt1, paramInt2, paramMemory, paramInstance);
        case 325:
          return Wat2WasmModuleMachineFuncGroup_0.func_325(paramInt1, paramInt2, paramMemory, paramInstance);
        case 326:
          return Wat2WasmModuleMachineFuncGroup_0.func_326(paramInt1, paramInt2, paramMemory, paramInstance);
        case 333:
          return Wat2WasmModuleMachineFuncGroup_0.func_333(paramInt1, paramInt2, paramMemory, paramInstance);
        case 353:
          return Wat2WasmModuleMachineFuncGroup_0.func_353(paramInt1, paramInt2, paramMemory, paramInstance);
        case 371:
          return Wat2WasmModuleMachineFuncGroup_0.func_371(paramInt1, paramInt2, paramMemory, paramInstance);
        case 384:
          return Wat2WasmModuleMachineFuncGroup_0.func_384(paramInt1, paramInt2, paramMemory, paramInstance);
        case 386:
          return Wat2WasmModuleMachineFuncGroup_0.func_386(paramInt1, paramInt2, paramMemory, paramInstance);
        case 396:
          return Wat2WasmModuleMachineFuncGroup_0.func_396(paramInt1, paramInt2, paramMemory, paramInstance);
        case 398:
          return Wat2WasmModuleMachineFuncGroup_0.func_398(paramInt1, paramInt2, paramMemory, paramInstance);
        case 399:
          return Wat2WasmModuleMachineFuncGroup_0.func_399(paramInt1, paramInt2, paramMemory, paramInstance);
        case 415:
          return Wat2WasmModuleMachineFuncGroup_0.func_415(paramInt1, paramInt2, paramMemory, paramInstance);
        case 418:
          return Wat2WasmModuleMachineFuncGroup_0.func_418(paramInt1, paramInt2, paramMemory, paramInstance);
        case 423:
          return Wat2WasmModuleMachineFuncGroup_0.func_423(paramInt1, paramInt2, paramMemory, paramInstance);
        case 439:
          return Wat2WasmModuleMachineFuncGroup_0.func_439(paramInt1, paramInt2, paramMemory, paramInstance);
        case 445:
          return Wat2WasmModuleMachineFuncGroup_0.func_445(paramInt1, paramInt2, paramMemory, paramInstance);
        case 446:
          return Wat2WasmModuleMachineFuncGroup_0.func_446(paramInt1, paramInt2, paramMemory, paramInstance);
        case 447:
          return Wat2WasmModuleMachineFuncGroup_0.func_447(paramInt1, paramInt2, paramMemory, paramInstance);
        case 448:
          return Wat2WasmModuleMachineFuncGroup_0.func_448(paramInt1, paramInt2, paramMemory, paramInstance);
        case 450:
          return Wat2WasmModuleMachineFuncGroup_0.func_450(paramInt1, paramInt2, paramMemory, paramInstance);
        case 453:
          return Wat2WasmModuleMachineFuncGroup_0.func_453(paramInt1, paramInt2, paramMemory, paramInstance);
        case 454:
          return Wat2WasmModuleMachineFuncGroup_0.func_454(paramInt1, paramInt2, paramMemory, paramInstance);
        case 461:
          return Wat2WasmModuleMachineFuncGroup_0.func_461(paramInt1, paramInt2, paramMemory, paramInstance);
        case 462:
          return Wat2WasmModuleMachineFuncGroup_0.func_462(paramInt1, paramInt2, paramMemory, paramInstance);
        case 463:
          return Wat2WasmModuleMachineFuncGroup_0.func_463(paramInt1, paramInt2, paramMemory, paramInstance);
        case 464:
          return Wat2WasmModuleMachineFuncGroup_0.func_464(paramInt1, paramInt2, paramMemory, paramInstance);
        case 465:
          return Wat2WasmModuleMachineFuncGroup_0.func_465(paramInt1, paramInt2, paramMemory, paramInstance);
        case 466:
          return Wat2WasmModuleMachineFuncGroup_0.func_466(paramInt1, paramInt2, paramMemory, paramInstance);
        case 467:
          return Wat2WasmModuleMachineFuncGroup_0.func_467(paramInt1, paramInt2, paramMemory, paramInstance);
        case 468:
          return Wat2WasmModuleMachineFuncGroup_0.func_468(paramInt1, paramInt2, paramMemory, paramInstance);
        case 469:
          return Wat2WasmModuleMachineFuncGroup_0.func_469(paramInt1, paramInt2, paramMemory, paramInstance);
        case 470:
          return Wat2WasmModuleMachineFuncGroup_0.func_470(paramInt1, paramInt2, paramMemory, paramInstance);
        case 471:
          return Wat2WasmModuleMachineFuncGroup_0.func_471(paramInt1, paramInt2, paramMemory, paramInstance);
        case 472:
          return Wat2WasmModuleMachineFuncGroup_0.func_472(paramInt1, paramInt2, paramMemory, paramInstance);
        case 473:
          return Wat2WasmModuleMachineFuncGroup_0.func_473(paramInt1, paramInt2, paramMemory, paramInstance);
        case 474:
          return Wat2WasmModuleMachineFuncGroup_0.func_474(paramInt1, paramInt2, paramMemory, paramInstance);
        case 475:
          return Wat2WasmModuleMachineFuncGroup_0.func_475(paramInt1, paramInt2, paramMemory, paramInstance);
        case 476:
          return Wat2WasmModuleMachineFuncGroup_0.func_476(paramInt1, paramInt2, paramMemory, paramInstance);
        case 477:
          return Wat2WasmModuleMachineFuncGroup_0.func_477(paramInt1, paramInt2, paramMemory, paramInstance);
        case 478:
          return Wat2WasmModuleMachineFuncGroup_0.func_478(paramInt1, paramInt2, paramMemory, paramInstance);
        case 479:
          return Wat2WasmModuleMachineFuncGroup_0.func_479(paramInt1, paramInt2, paramMemory, paramInstance);
        case 480:
          return Wat2WasmModuleMachineFuncGroup_0.func_480(paramInt1, paramInt2, paramMemory, paramInstance);
        case 481:
          return Wat2WasmModuleMachineFuncGroup_0.func_481(paramInt1, paramInt2, paramMemory, paramInstance);
        case 482:
          return Wat2WasmModuleMachineFuncGroup_0.func_482(paramInt1, paramInt2, paramMemory, paramInstance);
        case 483:
          return Wat2WasmModuleMachineFuncGroup_0.func_483(paramInt1, paramInt2, paramMemory, paramInstance);
        case 484:
          return Wat2WasmModuleMachineFuncGroup_0.func_484(paramInt1, paramInt2, paramMemory, paramInstance);
        case 485:
          return Wat2WasmModuleMachineFuncGroup_0.func_485(paramInt1, paramInt2, paramMemory, paramInstance);
        case 486:
          return Wat2WasmModuleMachineFuncGroup_0.func_486(paramInt1, paramInt2, paramMemory, paramInstance);
        case 487:
          return Wat2WasmModuleMachineFuncGroup_0.func_487(paramInt1, paramInt2, paramMemory, paramInstance);
        case 488:
          return Wat2WasmModuleMachineFuncGroup_0.func_488(paramInt1, paramInt2, paramMemory, paramInstance);
        case 489:
          return Wat2WasmModuleMachineFuncGroup_0.func_489(paramInt1, paramInt2, paramMemory, paramInstance);
        case 490:
          return Wat2WasmModuleMachineFuncGroup_0.func_490(paramInt1, paramInt2, paramMemory, paramInstance);
        case 491:
          return Wat2WasmModuleMachineFuncGroup_0.func_491(paramInt1, paramInt2, paramMemory, paramInstance);
        case 492:
          return Wat2WasmModuleMachineFuncGroup_0.func_492(paramInt1, paramInt2, paramMemory, paramInstance);
        case 493:
          return Wat2WasmModuleMachineFuncGroup_0.func_493(paramInt1, paramInt2, paramMemory, paramInstance);
        case 494:
          return Wat2WasmModuleMachineFuncGroup_0.func_494(paramInt1, paramInt2, paramMemory, paramInstance);
        case 495:
          return Wat2WasmModuleMachineFuncGroup_0.func_495(paramInt1, paramInt2, paramMemory, paramInstance);
        case 496:
          return Wat2WasmModuleMachineFuncGroup_0.func_496(paramInt1, paramInt2, paramMemory, paramInstance);
        case 497:
          return Wat2WasmModuleMachineFuncGroup_0.func_497(paramInt1, paramInt2, paramMemory, paramInstance);
        case 498:
          return Wat2WasmModuleMachineFuncGroup_0.func_498(paramInt1, paramInt2, paramMemory, paramInstance);
        case 499:
          return Wat2WasmModuleMachineFuncGroup_0.func_499(paramInt1, paramInt2, paramMemory, paramInstance);
        case 500:
          return Wat2WasmModuleMachineFuncGroup_0.func_500(paramInt1, paramInt2, paramMemory, paramInstance);
        case 501:
          return Wat2WasmModuleMachineFuncGroup_0.func_501(paramInt1, paramInt2, paramMemory, paramInstance);
        case 502:
          return Wat2WasmModuleMachineFuncGroup_0.func_502(paramInt1, paramInt2, paramMemory, paramInstance);
        case 503:
          return Wat2WasmModuleMachineFuncGroup_0.func_503(paramInt1, paramInt2, paramMemory, paramInstance);
        case 504:
          return Wat2WasmModuleMachineFuncGroup_0.func_504(paramInt1, paramInt2, paramMemory, paramInstance);
        case 505:
          return Wat2WasmModuleMachineFuncGroup_0.func_505(paramInt1, paramInt2, paramMemory, paramInstance);
        case 506:
          return Wat2WasmModuleMachineFuncGroup_0.func_506(paramInt1, paramInt2, paramMemory, paramInstance);
        case 507:
          return Wat2WasmModuleMachineFuncGroup_0.func_507(paramInt1, paramInt2, paramMemory, paramInstance);
        case 508:
          return Wat2WasmModuleMachineFuncGroup_0.func_508(paramInt1, paramInt2, paramMemory, paramInstance);
        case 509:
          return Wat2WasmModuleMachineFuncGroup_0.func_509(paramInt1, paramInt2, paramMemory, paramInstance);
        case 510:
          return Wat2WasmModuleMachineFuncGroup_0.func_510(paramInt1, paramInt2, paramMemory, paramInstance);
        case 511:
          return Wat2WasmModuleMachineFuncGroup_0.func_511(paramInt1, paramInt2, paramMemory, paramInstance);
        case 512:
          return Wat2WasmModuleMachineFuncGroup_0.func_512(paramInt1, paramInt2, paramMemory, paramInstance);
        case 513:
          return Wat2WasmModuleMachineFuncGroup_0.func_513(paramInt1, paramInt2, paramMemory, paramInstance);
        case 515:
          return Wat2WasmModuleMachineFuncGroup_0.func_515(paramInt1, paramInt2, paramMemory, paramInstance);
        case 516:
          return Wat2WasmModuleMachineFuncGroup_0.func_516(paramInt1, paramInt2, paramMemory, paramInstance);
        case 517:
          return Wat2WasmModuleMachineFuncGroup_0.func_517(paramInt1, paramInt2, paramMemory, paramInstance);
        case 518:
          return Wat2WasmModuleMachineFuncGroup_0.func_518(paramInt1, paramInt2, paramMemory, paramInstance);
        case 519:
          return Wat2WasmModuleMachineFuncGroup_0.func_519(paramInt1, paramInt2, paramMemory, paramInstance);
        case 520:
          return Wat2WasmModuleMachineFuncGroup_0.func_520(paramInt1, paramInt2, paramMemory, paramInstance);
        case 521:
          return Wat2WasmModuleMachineFuncGroup_0.func_521(paramInt1, paramInt2, paramMemory, paramInstance);
        case 522:
          return Wat2WasmModuleMachineFuncGroup_0.func_522(paramInt1, paramInt2, paramMemory, paramInstance);
        case 523:
          return Wat2WasmModuleMachineFuncGroup_0.func_523(paramInt1, paramInt2, paramMemory, paramInstance);
        case 524:
          return Wat2WasmModuleMachineFuncGroup_0.func_524(paramInt1, paramInt2, paramMemory, paramInstance);
        case 525:
          return Wat2WasmModuleMachineFuncGroup_0.func_525(paramInt1, paramInt2, paramMemory, paramInstance);
        case 526:
          return Wat2WasmModuleMachineFuncGroup_0.func_526(paramInt1, paramInt2, paramMemory, paramInstance);
        case 527:
          return Wat2WasmModuleMachineFuncGroup_0.func_527(paramInt1, paramInt2, paramMemory, paramInstance);
        case 528:
          return Wat2WasmModuleMachineFuncGroup_0.func_528(paramInt1, paramInt2, paramMemory, paramInstance);
        case 529:
          return Wat2WasmModuleMachineFuncGroup_0.func_529(paramInt1, paramInt2, paramMemory, paramInstance);
        case 530:
          return Wat2WasmModuleMachineFuncGroup_0.func_530(paramInt1, paramInt2, paramMemory, paramInstance);
        case 531:
          return Wat2WasmModuleMachineFuncGroup_0.func_531(paramInt1, paramInt2, paramMemory, paramInstance);
        case 532:
          return Wat2WasmModuleMachineFuncGroup_0.func_532(paramInt1, paramInt2, paramMemory, paramInstance);
        case 533:
          return Wat2WasmModuleMachineFuncGroup_0.func_533(paramInt1, paramInt2, paramMemory, paramInstance);
        case 546:
          return Wat2WasmModuleMachineFuncGroup_0.func_546(paramInt1, paramInt2, paramMemory, paramInstance);
        case 570:
          return Wat2WasmModuleMachineFuncGroup_0.func_570(paramInt1, paramInt2, paramMemory, paramInstance);
        case 571:
          return Wat2WasmModuleMachineFuncGroup_0.func_571(paramInt1, paramInt2, paramMemory, paramInstance);
        case 575:
          return Wat2WasmModuleMachineFuncGroup_0.func_575(paramInt1, paramInt2, paramMemory, paramInstance);
        case 577:
          return Wat2WasmModuleMachineFuncGroup_0.func_577(paramInt1, paramInt2, paramMemory, paramInstance);
        case 595:
          return Wat2WasmModuleMachineFuncGroup_0.func_595(paramInt1, paramInt2, paramMemory, paramInstance);
        case 596:
          return Wat2WasmModuleMachineFuncGroup_0.func_596(paramInt1, paramInt2, paramMemory, paramInstance);
        case 597:
          return Wat2WasmModuleMachineFuncGroup_0.func_597(paramInt1, paramInt2, paramMemory, paramInstance);
        case 599:
          return Wat2WasmModuleMachineFuncGroup_0.func_599(paramInt1, paramInt2, paramMemory, paramInstance);
        case 601:
          return Wat2WasmModuleMachineFuncGroup_0.func_601(paramInt1, paramInt2, paramMemory, paramInstance);
        case 602:
          return Wat2WasmModuleMachineFuncGroup_0.func_602(paramInt1, paramInt2, paramMemory, paramInstance);
        case 604:
          return Wat2WasmModuleMachineFuncGroup_0.func_604(paramInt1, paramInt2, paramMemory, paramInstance);
        case 605:
          return Wat2WasmModuleMachineFuncGroup_0.func_605(paramInt1, paramInt2, paramMemory, paramInstance);
        case 607:
          return Wat2WasmModuleMachineFuncGroup_0.func_607(paramInt1, paramInt2, paramMemory, paramInstance);
        case 615:
          return Wat2WasmModuleMachineFuncGroup_0.func_615(paramInt1, paramInt2, paramMemory, paramInstance);
        case 623:
          return Wat2WasmModuleMachineFuncGroup_0.func_623(paramInt1, paramInt2, paramMemory, paramInstance);
        case 644:
          return Wat2WasmModuleMachineFuncGroup_0.func_644(paramInt1, paramInt2, paramMemory, paramInstance);
        case 645:
          return Wat2WasmModuleMachineFuncGroup_0.func_645(paramInt1, paramInt2, paramMemory, paramInstance);
        case 647:
          return Wat2WasmModuleMachineFuncGroup_0.func_647(paramInt1, paramInt2, paramMemory, paramInstance);
        case 648:
          return Wat2WasmModuleMachineFuncGroup_0.func_648(paramInt1, paramInt2, paramMemory, paramInstance);
        case 650:
          return Wat2WasmModuleMachineFuncGroup_0.func_650(paramInt1, paramInt2, paramMemory, paramInstance);
        case 651:
          return Wat2WasmModuleMachineFuncGroup_0.func_651(paramInt1, paramInt2, paramMemory, paramInstance);
        case 654:
          return Wat2WasmModuleMachineFuncGroup_0.func_654(paramInt1, paramInt2, paramMemory, paramInstance);
        case 655:
          return Wat2WasmModuleMachineFuncGroup_0.func_655(paramInt1, paramInt2, paramMemory, paramInstance);
        case 657:
          return Wat2WasmModuleMachineFuncGroup_0.func_657(paramInt1, paramInt2, paramMemory, paramInstance);
        case 658:
          return Wat2WasmModuleMachineFuncGroup_0.func_658(paramInt1, paramInt2, paramMemory, paramInstance);
        case 660:
          return Wat2WasmModuleMachineFuncGroup_0.func_660(paramInt1, paramInt2, paramMemory, paramInstance);
        case 661:
          return Wat2WasmModuleMachineFuncGroup_0.func_661(paramInt1, paramInt2, paramMemory, paramInstance);
        case 662:
          return Wat2WasmModuleMachineFuncGroup_0.func_662(paramInt1, paramInt2, paramMemory, paramInstance);
        case 663:
          return Wat2WasmModuleMachineFuncGroup_0.func_663(paramInt1, paramInt2, paramMemory, paramInstance);
        case 664:
          return Wat2WasmModuleMachineFuncGroup_0.func_664(paramInt1, paramInt2, paramMemory, paramInstance);
        case 666:
          return Wat2WasmModuleMachineFuncGroup_0.func_666(paramInt1, paramInt2, paramMemory, paramInstance);
        case 667:
          return Wat2WasmModuleMachineFuncGroup_0.func_667(paramInt1, paramInt2, paramMemory, paramInstance);
        case 669:
          return Wat2WasmModuleMachineFuncGroup_0.func_669(paramInt1, paramInt2, paramMemory, paramInstance);
        case 670:
          return Wat2WasmModuleMachineFuncGroup_0.func_670(paramInt1, paramInt2, paramMemory, paramInstance);
        case 672:
          return Wat2WasmModuleMachineFuncGroup_0.func_672(paramInt1, paramInt2, paramMemory, paramInstance);
        case 673:
          return Wat2WasmModuleMachineFuncGroup_0.func_673(paramInt1, paramInt2, paramMemory, paramInstance);
        case 674:
          return Wat2WasmModuleMachineFuncGroup_0.func_674(paramInt1, paramInt2, paramMemory, paramInstance);
        case 675:
          return Wat2WasmModuleMachineFuncGroup_0.func_675(paramInt1, paramInt2, paramMemory, paramInstance);
        case 682:
          return Wat2WasmModuleMachineFuncGroup_0.func_682(paramInt1, paramInt2, paramMemory, paramInstance);
        case 684:
          return Wat2WasmModuleMachineFuncGroup_0.func_684(paramInt1, paramInt2, paramMemory, paramInstance);
        case 685:
          return Wat2WasmModuleMachineFuncGroup_0.func_685(paramInt1, paramInt2, paramMemory, paramInstance);
        case 688:
          return Wat2WasmModuleMachineFuncGroup_0.func_688(paramInt1, paramInt2, paramMemory, paramInstance);
        case 690:
          return Wat2WasmModuleMachineFuncGroup_0.func_690(paramInt1, paramInt2, paramMemory, paramInstance);
        case 691:
          return Wat2WasmModuleMachineFuncGroup_0.func_691(paramInt1, paramInt2, paramMemory, paramInstance);
        case 692:
          return Wat2WasmModuleMachineFuncGroup_0.func_692(paramInt1, paramInt2, paramMemory, paramInstance);
        case 696:
          return Wat2WasmModuleMachineFuncGroup_0.func_696(paramInt1, paramInt2, paramMemory, paramInstance);
        case 697:
          return Wat2WasmModuleMachineFuncGroup_0.func_697(paramInt1, paramInt2, paramMemory, paramInstance);
        case 699:
          return Wat2WasmModuleMachineFuncGroup_0.func_699(paramInt1, paramInt2, paramMemory, paramInstance);
        case 700:
          return Wat2WasmModuleMachineFuncGroup_0.func_700(paramInt1, paramInt2, paramMemory, paramInstance);
        case 701:
          return Wat2WasmModuleMachineFuncGroup_0.func_701(paramInt1, paramInt2, paramMemory, paramInstance);
        case 703:
          return Wat2WasmModuleMachineFuncGroup_0.func_703(paramInt1, paramInt2, paramMemory, paramInstance);
        case 704:
          return Wat2WasmModuleMachineFuncGroup_0.func_704(paramInt1, paramInt2, paramMemory, paramInstance);
        case 705:
          return Wat2WasmModuleMachineFuncGroup_0.func_705(paramInt1, paramInt2, paramMemory, paramInstance);
        case 707:
          return Wat2WasmModuleMachineFuncGroup_0.func_707(paramInt1, paramInt2, paramMemory, paramInstance);
        case 709:
          return Wat2WasmModuleMachineFuncGroup_0.func_709(paramInt1, paramInt2, paramMemory, paramInstance);
        case 711:
          return Wat2WasmModuleMachineFuncGroup_0.func_711(paramInt1, paramInt2, paramMemory, paramInstance);
        case 712:
          return Wat2WasmModuleMachineFuncGroup_0.func_712(paramInt1, paramInt2, paramMemory, paramInstance);
        case 713:
          return Wat2WasmModuleMachineFuncGroup_0.func_713(paramInt1, paramInt2, paramMemory, paramInstance);
        case 714:
          return Wat2WasmModuleMachineFuncGroup_0.func_714(paramInt1, paramInt2, paramMemory, paramInstance);
        case 715:
          return Wat2WasmModuleMachineFuncGroup_0.func_715(paramInt1, paramInt2, paramMemory, paramInstance);
        case 716:
          return Wat2WasmModuleMachineFuncGroup_0.func_716(paramInt1, paramInt2, paramMemory, paramInstance);
        case 717:
          return Wat2WasmModuleMachineFuncGroup_0.func_717(paramInt1, paramInt2, paramMemory, paramInstance);
        case 720:
          return Wat2WasmModuleMachineFuncGroup_0.func_720(paramInt1, paramInt2, paramMemory, paramInstance);
        case 721:
          return Wat2WasmModuleMachineFuncGroup_0.func_721(paramInt1, paramInt2, paramMemory, paramInstance);
        case 727:
          return Wat2WasmModuleMachineFuncGroup_0.func_727(paramInt1, paramInt2, paramMemory, paramInstance);
        case 730:
          return Wat2WasmModuleMachineFuncGroup_0.func_730(paramInt1, paramInt2, paramMemory, paramInstance);
        case 731:
          return Wat2WasmModuleMachineFuncGroup_0.func_731(paramInt1, paramInt2, paramMemory, paramInstance);
        case 735:
          return Wat2WasmModuleMachineFuncGroup_0.func_735(paramInt1, paramInt2, paramMemory, paramInstance);
        case 736:
          return Wat2WasmModuleMachineFuncGroup_0.func_736(paramInt1, paramInt2, paramMemory, paramInstance);
        case 737:
          return Wat2WasmModuleMachineFuncGroup_0.func_737(paramInt1, paramInt2, paramMemory, paramInstance);
        case 738:
          return Wat2WasmModuleMachineFuncGroup_0.func_738(paramInt1, paramInt2, paramMemory, paramInstance);
        case 742:
          return Wat2WasmModuleMachineFuncGroup_0.func_742(paramInt1, paramInt2, paramMemory, paramInstance);
        case 744:
          return Wat2WasmModuleMachineFuncGroup_0.func_744(paramInt1, paramInt2, paramMemory, paramInstance);
        case 745:
          return Wat2WasmModuleMachineFuncGroup_0.func_745(paramInt1, paramInt2, paramMemory, paramInstance);
        case 747:
          return Wat2WasmModuleMachineFuncGroup_0.func_747(paramInt1, paramInt2, paramMemory, paramInstance);
        case 748:
          return Wat2WasmModuleMachineFuncGroup_0.func_748(paramInt1, paramInt2, paramMemory, paramInstance);
        case 749:
          return Wat2WasmModuleMachineFuncGroup_0.func_749(paramInt1, paramInt2, paramMemory, paramInstance);
        case 751:
          return Wat2WasmModuleMachineFuncGroup_0.func_751(paramInt1, paramInt2, paramMemory, paramInstance);
        case 752:
          return Wat2WasmModuleMachineFuncGroup_0.func_752(paramInt1, paramInt2, paramMemory, paramInstance);
        case 754:
          return Wat2WasmModuleMachineFuncGroup_0.func_754(paramInt1, paramInt2, paramMemory, paramInstance);
        case 755:
          return Wat2WasmModuleMachineFuncGroup_0.func_755(paramInt1, paramInt2, paramMemory, paramInstance);
        case 756:
          return Wat2WasmModuleMachineFuncGroup_0.func_756(paramInt1, paramInt2, paramMemory, paramInstance);
        case 758:
          return Wat2WasmModuleMachineFuncGroup_0.func_758(paramInt1, paramInt2, paramMemory, paramInstance);
        case 760:
          return Wat2WasmModuleMachineFuncGroup_0.func_760(paramInt1, paramInt2, paramMemory, paramInstance);
        case 762:
          return Wat2WasmModuleMachineFuncGroup_0.func_762(paramInt1, paramInt2, paramMemory, paramInstance);
        case 763:
          return Wat2WasmModuleMachineFuncGroup_0.func_763(paramInt1, paramInt2, paramMemory, paramInstance);
        case 764:
          return Wat2WasmModuleMachineFuncGroup_0.func_764(paramInt1, paramInt2, paramMemory, paramInstance);
        case 765:
          return Wat2WasmModuleMachineFuncGroup_0.func_765(paramInt1, paramInt2, paramMemory, paramInstance);
        case 767:
          return Wat2WasmModuleMachineFuncGroup_0.func_767(paramInt1, paramInt2, paramMemory, paramInstance);
        case 768:
          return Wat2WasmModuleMachineFuncGroup_0.func_768(paramInt1, paramInt2, paramMemory, paramInstance);
        case 770:
          return Wat2WasmModuleMachineFuncGroup_0.func_770(paramInt1, paramInt2, paramMemory, paramInstance);
        case 771:
          return Wat2WasmModuleMachineFuncGroup_0.func_771(paramInt1, paramInt2, paramMemory, paramInstance);
        case 772:
          return Wat2WasmModuleMachineFuncGroup_0.func_772(paramInt1, paramInt2, paramMemory, paramInstance);
        case 773:
          return Wat2WasmModuleMachineFuncGroup_0.func_773(paramInt1, paramInt2, paramMemory, paramInstance);
        case 774:
          return Wat2WasmModuleMachineFuncGroup_0.func_774(paramInt1, paramInt2, paramMemory, paramInstance);
        case 776:
          return Wat2WasmModuleMachineFuncGroup_0.func_776(paramInt1, paramInt2, paramMemory, paramInstance);
        case 778:
          return Wat2WasmModuleMachineFuncGroup_0.func_778(paramInt1, paramInt2, paramMemory, paramInstance);
        case 779:
          return Wat2WasmModuleMachineFuncGroup_0.func_779(paramInt1, paramInt2, paramMemory, paramInstance);
        case 782:
          return Wat2WasmModuleMachineFuncGroup_0.func_782(paramInt1, paramInt2, paramMemory, paramInstance);
        case 785:
          return Wat2WasmModuleMachineFuncGroup_0.func_785(paramInt1, paramInt2, paramMemory, paramInstance);
        case 787:
          return Wat2WasmModuleMachineFuncGroup_0.func_787(paramInt1, paramInt2, paramMemory, paramInstance);
        case 789:
          return Wat2WasmModuleMachineFuncGroup_0.func_789(paramInt1, paramInt2, paramMemory, paramInstance);
        case 794:
          return Wat2WasmModuleMachineFuncGroup_0.func_794(paramInt1, paramInt2, paramMemory, paramInstance);
        case 796:
          return Wat2WasmModuleMachineFuncGroup_0.func_796(paramInt1, paramInt2, paramMemory, paramInstance);
        case 797:
          return Wat2WasmModuleMachineFuncGroup_0.func_797(paramInt1, paramInt2, paramMemory, paramInstance);
        case 798:
          return Wat2WasmModuleMachineFuncGroup_0.func_798(paramInt1, paramInt2, paramMemory, paramInstance);
        case 810:
          return Wat2WasmModuleMachineFuncGroup_0.func_810(paramInt1, paramInt2, paramMemory, paramInstance);
        case 819:
          return Wat2WasmModuleMachineFuncGroup_0.func_819(paramInt1, paramInt2, paramMemory, paramInstance);
        case 821:
          return Wat2WasmModuleMachineFuncGroup_0.func_821(paramInt1, paramInt2, paramMemory, paramInstance);
        case 823:
          return Wat2WasmModuleMachineFuncGroup_0.func_823(paramInt1, paramInt2, paramMemory, paramInstance);
        case 824:
          return Wat2WasmModuleMachineFuncGroup_0.func_824(paramInt1, paramInt2, paramMemory, paramInstance);
        case 825:
          return Wat2WasmModuleMachineFuncGroup_0.func_825(paramInt1, paramInt2, paramMemory, paramInstance);
        case 842:
          return Wat2WasmModuleMachineFuncGroup_0.func_842(paramInt1, paramInt2, paramMemory, paramInstance);
        case 845:
          return Wat2WasmModuleMachineFuncGroup_0.func_845(paramInt1, paramInt2, paramMemory, paramInstance);
        case 850:
          return Wat2WasmModuleMachineFuncGroup_0.func_850(paramInt1, paramInt2, paramMemory, paramInstance);
        case 851:
          return Wat2WasmModuleMachineFuncGroup_0.func_851(paramInt1, paramInt2, paramMemory, paramInstance);
        case 858:
          return Wat2WasmModuleMachineFuncGroup_0.func_858(paramInt1, paramInt2, paramMemory, paramInstance);
        case 859:
          return Wat2WasmModuleMachineFuncGroup_0.func_859(paramInt1, paramInt2, paramMemory, paramInstance);
        case 868:
          return Wat2WasmModuleMachineFuncGroup_0.func_868(paramInt1, paramInt2, paramMemory, paramInstance);
        case 869:
          return Wat2WasmModuleMachineFuncGroup_0.func_869(paramInt1, paramInt2, paramMemory, paramInstance);
        case 872:
          return Wat2WasmModuleMachineFuncGroup_0.func_872(paramInt1, paramInt2, paramMemory, paramInstance);
        case 873:
          return Wat2WasmModuleMachineFuncGroup_0.func_873(paramInt1, paramInt2, paramMemory, paramInstance);
        case 876:
          return Wat2WasmModuleMachineFuncGroup_0.func_876(paramInt1, paramInt2, paramMemory, paramInstance);
        case 877:
          return Wat2WasmModuleMachineFuncGroup_0.func_877(paramInt1, paramInt2, paramMemory, paramInstance);
        case 880:
          return Wat2WasmModuleMachineFuncGroup_0.func_880(paramInt1, paramInt2, paramMemory, paramInstance);
        case 881:
          return Wat2WasmModuleMachineFuncGroup_0.func_881(paramInt1, paramInt2, paramMemory, paramInstance);
        case 883:
          return Wat2WasmModuleMachineFuncGroup_0.func_883(paramInt1, paramInt2, paramMemory, paramInstance);
        case 885:
          return Wat2WasmModuleMachineFuncGroup_0.func_885(paramInt1, paramInt2, paramMemory, paramInstance);
        case 886:
          return Wat2WasmModuleMachineFuncGroup_0.func_886(paramInt1, paramInt2, paramMemory, paramInstance);
        case 888:
          return Wat2WasmModuleMachineFuncGroup_0.func_888(paramInt1, paramInt2, paramMemory, paramInstance);
        case 889:
          return Wat2WasmModuleMachineFuncGroup_0.func_889(paramInt1, paramInt2, paramMemory, paramInstance);
        case 892:
          return Wat2WasmModuleMachineFuncGroup_0.func_892(paramInt1, paramInt2, paramMemory, paramInstance);
        case 893:
          return Wat2WasmModuleMachineFuncGroup_0.func_893(paramInt1, paramInt2, paramMemory, paramInstance);
        case 895:
          return Wat2WasmModuleMachineFuncGroup_0.func_895(paramInt1, paramInt2, paramMemory, paramInstance);
        case 896:
          return Wat2WasmModuleMachineFuncGroup_0.func_896(paramInt1, paramInt2, paramMemory, paramInstance);
        case 898:
          return Wat2WasmModuleMachineFuncGroup_0.func_898(paramInt1, paramInt2, paramMemory, paramInstance);
        case 902:
          return Wat2WasmModuleMachineFuncGroup_0.func_902(paramInt1, paramInt2, paramMemory, paramInstance);
        case 904:
          return Wat2WasmModuleMachineFuncGroup_0.func_904(paramInt1, paramInt2, paramMemory, paramInstance);
        case 905:
          return Wat2WasmModuleMachineFuncGroup_0.func_905(paramInt1, paramInt2, paramMemory, paramInstance);
        case 911:
          return Wat2WasmModuleMachineFuncGroup_0.func_911(paramInt1, paramInt2, paramMemory, paramInstance);
        case 913:
          return Wat2WasmModuleMachineFuncGroup_0.func_913(paramInt1, paramInt2, paramMemory, paramInstance);
        case 914:
          return Wat2WasmModuleMachineFuncGroup_0.func_914(paramInt1, paramInt2, paramMemory, paramInstance);
        case 915:
          return Wat2WasmModuleMachineFuncGroup_0.func_915(paramInt1, paramInt2, paramMemory, paramInstance);
        case 921:
          return Wat2WasmModuleMachineFuncGroup_0.func_921(paramInt1, paramInt2, paramMemory, paramInstance);
        case 923:
          return Wat2WasmModuleMachineFuncGroup_0.func_923(paramInt1, paramInt2, paramMemory, paramInstance);
        case 924:
          return Wat2WasmModuleMachineFuncGroup_0.func_924(paramInt1, paramInt2, paramMemory, paramInstance);
        case 926:
          return Wat2WasmModuleMachineFuncGroup_0.func_926(paramInt1, paramInt2, paramMemory, paramInstance);
        case 927:
          return Wat2WasmModuleMachineFuncGroup_0.func_927(paramInt1, paramInt2, paramMemory, paramInstance);
        case 930:
          return Wat2WasmModuleMachineFuncGroup_0.func_930(paramInt1, paramInt2, paramMemory, paramInstance);
        case 933:
          return Wat2WasmModuleMachineFuncGroup_0.func_933(paramInt1, paramInt2, paramMemory, paramInstance);
        case 934:
          return Wat2WasmModuleMachineFuncGroup_0.func_934(paramInt1, paramInt2, paramMemory, paramInstance);
        case 936:
          return Wat2WasmModuleMachineFuncGroup_0.func_936(paramInt1, paramInt2, paramMemory, paramInstance);
        case 937:
          return Wat2WasmModuleMachineFuncGroup_0.func_937(paramInt1, paramInt2, paramMemory, paramInstance);
        case 938:
          return Wat2WasmModuleMachineFuncGroup_0.func_938(paramInt1, paramInt2, paramMemory, paramInstance);
        case 942:
          return Wat2WasmModuleMachineFuncGroup_0.func_942(paramInt1, paramInt2, paramMemory, paramInstance);
        case 944:
          return Wat2WasmModuleMachineFuncGroup_0.func_944(paramInt1, paramInt2, paramMemory, paramInstance);
        case 945:
          return Wat2WasmModuleMachineFuncGroup_0.func_945(paramInt1, paramInt2, paramMemory, paramInstance);
        case 946:
          return Wat2WasmModuleMachineFuncGroup_0.func_946(paramInt1, paramInt2, paramMemory, paramInstance);
        case 947:
          return Wat2WasmModuleMachineFuncGroup_0.func_947(paramInt1, paramInt2, paramMemory, paramInstance);
        case 949:
          return Wat2WasmModuleMachineFuncGroup_0.func_949(paramInt1, paramInt2, paramMemory, paramInstance);
        case 951:
          return Wat2WasmModuleMachineFuncGroup_0.func_951(paramInt1, paramInt2, paramMemory, paramInstance);
        case 952:
          return Wat2WasmModuleMachineFuncGroup_0.func_952(paramInt1, paramInt2, paramMemory, paramInstance);
        case 953:
          return Wat2WasmModuleMachineFuncGroup_0.func_953(paramInt1, paramInt2, paramMemory, paramInstance);
        case 954:
          return Wat2WasmModuleMachineFuncGroup_0.func_954(paramInt1, paramInt2, paramMemory, paramInstance);
        case 956:
          return Wat2WasmModuleMachineFuncGroup_0.func_956(paramInt1, paramInt2, paramMemory, paramInstance);
        case 957:
          return Wat2WasmModuleMachineFuncGroup_0.func_957(paramInt1, paramInt2, paramMemory, paramInstance);
        case 958:
          return Wat2WasmModuleMachineFuncGroup_0.func_958(paramInt1, paramInt2, paramMemory, paramInstance);
        case 960:
          return Wat2WasmModuleMachineFuncGroup_0.func_960(paramInt1, paramInt2, paramMemory, paramInstance);
        case 962:
          return Wat2WasmModuleMachineFuncGroup_0.func_962(paramInt1, paramInt2, paramMemory, paramInstance);
        case 964:
          return Wat2WasmModuleMachineFuncGroup_0.func_964(paramInt1, paramInt2, paramMemory, paramInstance);
        case 965:
          return Wat2WasmModuleMachineFuncGroup_0.func_965(paramInt1, paramInt2, paramMemory, paramInstance);
        case 966:
          return Wat2WasmModuleMachineFuncGroup_0.func_966(paramInt1, paramInt2, paramMemory, paramInstance);
        case 967:
          return Wat2WasmModuleMachineFuncGroup_0.func_967(paramInt1, paramInt2, paramMemory, paramInstance);
        case 968:
          return Wat2WasmModuleMachineFuncGroup_0.func_968(paramInt1, paramInt2, paramMemory, paramInstance);
        case 969:
          return Wat2WasmModuleMachineFuncGroup_0.func_969(paramInt1, paramInt2, paramMemory, paramInstance);
        case 970:
          return Wat2WasmModuleMachineFuncGroup_0.func_970(paramInt1, paramInt2, paramMemory, paramInstance);
        case 973:
          return Wat2WasmModuleMachineFuncGroup_0.func_973(paramInt1, paramInt2, paramMemory, paramInstance);
        case 975:
          return Wat2WasmModuleMachineFuncGroup_0.func_975(paramInt1, paramInt2, paramMemory, paramInstance);
        case 980:
          return Wat2WasmModuleMachineFuncGroup_0.func_980(paramInt1, paramInt2, paramMemory, paramInstance);
        case 982:
          return Wat2WasmModuleMachineFuncGroup_0.func_982(paramInt1, paramInt2, paramMemory, paramInstance);
        case 985:
          return Wat2WasmModuleMachineFuncGroup_0.func_985(paramInt1, paramInt2, paramMemory, paramInstance);
        case 986:
          return Wat2WasmModuleMachineFuncGroup_0.func_986(paramInt1, paramInt2, paramMemory, paramInstance);
        case 988:
          return Wat2WasmModuleMachineFuncGroup_0.func_988(paramInt1, paramInt2, paramMemory, paramInstance);
        case 996:
          return Wat2WasmModuleMachineFuncGroup_0.func_996(paramInt1, paramInt2, paramMemory, paramInstance);
        case 997:
          return Wat2WasmModuleMachineFuncGroup_0.func_997(paramInt1, paramInt2, paramMemory, paramInstance);
        case 999:
          return Wat2WasmModuleMachineFuncGroup_0.func_999(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1000:
          return Wat2WasmModuleMachineFuncGroup_0.func_1000(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1007:
          return Wat2WasmModuleMachineFuncGroup_0.func_1007(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1009:
          return Wat2WasmModuleMachineFuncGroup_0.func_1009(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1010:
          return Wat2WasmModuleMachineFuncGroup_0.func_1010(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1012:
          return Wat2WasmModuleMachineFuncGroup_0.func_1012(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1013:
          return Wat2WasmModuleMachineFuncGroup_0.func_1013(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1015:
          return Wat2WasmModuleMachineFuncGroup_0.func_1015(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1017:
          return Wat2WasmModuleMachineFuncGroup_0.func_1017(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1018:
          return Wat2WasmModuleMachineFuncGroup_0.func_1018(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1020:
          return Wat2WasmModuleMachineFuncGroup_0.func_1020(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1022:
          return Wat2WasmModuleMachineFuncGroup_0.func_1022(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1025:
          return Wat2WasmModuleMachineFuncGroup_0.func_1025(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1029:
          return Wat2WasmModuleMachineFuncGroup_0.func_1029(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1034:
          return Wat2WasmModuleMachineFuncGroup_0.func_1034(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1039:
          return Wat2WasmModuleMachineFuncGroup_0.func_1039(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1043:
          return Wat2WasmModuleMachineFuncGroup_0.func_1043(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1045:
          return Wat2WasmModuleMachineFuncGroup_0.func_1045(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1046:
          return Wat2WasmModuleMachineFuncGroup_0.func_1046(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1049:
          return Wat2WasmModuleMachineFuncGroup_0.func_1049(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1050:
          return Wat2WasmModuleMachineFuncGroup_0.func_1050(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1052:
          return Wat2WasmModuleMachineFuncGroup_0.func_1052(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1053:
          return Wat2WasmModuleMachineFuncGroup_0.func_1053(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1056:
          return Wat2WasmModuleMachineFuncGroup_0.func_1056(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1060:
          return Wat2WasmModuleMachineFuncGroup_0.func_1060(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1061:
          return Wat2WasmModuleMachineFuncGroup_0.func_1061(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1068:
          return Wat2WasmModuleMachineFuncGroup_0.func_1068(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1070:
          return Wat2WasmModuleMachineFuncGroup_0.func_1070(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1072:
          return Wat2WasmModuleMachineFuncGroup_0.func_1072(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1076:
          return Wat2WasmModuleMachineFuncGroup_0.func_1076(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1077:
          return Wat2WasmModuleMachineFuncGroup_0.func_1077(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1081:
          return Wat2WasmModuleMachineFuncGroup_0.func_1081(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1093:
          return Wat2WasmModuleMachineFuncGroup_0.func_1093(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1097:
          return Wat2WasmModuleMachineFuncGroup_0.func_1097(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1098:
          return Wat2WasmModuleMachineFuncGroup_0.func_1098(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1104:
          return Wat2WasmModuleMachineFuncGroup_0.func_1104(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1106:
          return Wat2WasmModuleMachineFuncGroup_0.func_1106(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1107:
          return Wat2WasmModuleMachineFuncGroup_0.func_1107(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1109:
          return Wat2WasmModuleMachineFuncGroup_0.func_1109(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1110:
          return Wat2WasmModuleMachineFuncGroup_0.func_1110(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1111:
          return Wat2WasmModuleMachineFuncGroup_0.func_1111(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1112:
          return Wat2WasmModuleMachineFuncGroup_0.func_1112(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1113:
          return Wat2WasmModuleMachineFuncGroup_0.func_1113(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1114:
          return Wat2WasmModuleMachineFuncGroup_0.func_1114(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1115:
          return Wat2WasmModuleMachineFuncGroup_0.func_1115(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1116:
          return Wat2WasmModuleMachineFuncGroup_0.func_1116(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1117:
          return Wat2WasmModuleMachineFuncGroup_0.func_1117(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1118:
          return Wat2WasmModuleMachineFuncGroup_0.func_1118(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1119:
          return Wat2WasmModuleMachineFuncGroup_0.func_1119(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1120:
          return Wat2WasmModuleMachineFuncGroup_0.func_1120(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1121:
          return Wat2WasmModuleMachineFuncGroup_0.func_1121(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1122:
          return Wat2WasmModuleMachineFuncGroup_0.func_1122(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1123:
          return Wat2WasmModuleMachineFuncGroup_0.func_1123(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1124:
          return Wat2WasmModuleMachineFuncGroup_0.func_1124(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1125:
          return Wat2WasmModuleMachineFuncGroup_0.func_1125(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1126:
          return Wat2WasmModuleMachineFuncGroup_0.func_1126(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1127:
          return Wat2WasmModuleMachineFuncGroup_0.func_1127(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1128:
          return Wat2WasmModuleMachineFuncGroup_0.func_1128(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1129:
          return Wat2WasmModuleMachineFuncGroup_0.func_1129(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1130:
          return Wat2WasmModuleMachineFuncGroup_0.func_1130(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1131:
          return Wat2WasmModuleMachineFuncGroup_0.func_1131(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1132:
          return Wat2WasmModuleMachineFuncGroup_0.func_1132(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1133:
          return Wat2WasmModuleMachineFuncGroup_0.func_1133(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1134:
          return Wat2WasmModuleMachineFuncGroup_0.func_1134(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1135:
          return Wat2WasmModuleMachineFuncGroup_0.func_1135(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1136:
          return Wat2WasmModuleMachineFuncGroup_0.func_1136(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1137:
          return Wat2WasmModuleMachineFuncGroup_0.func_1137(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1138:
          return Wat2WasmModuleMachineFuncGroup_0.func_1138(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1139:
          return Wat2WasmModuleMachineFuncGroup_0.func_1139(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1140:
          return Wat2WasmModuleMachineFuncGroup_0.func_1140(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1141:
          return Wat2WasmModuleMachineFuncGroup_0.func_1141(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1142:
          return Wat2WasmModuleMachineFuncGroup_0.func_1142(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1143:
          return Wat2WasmModuleMachineFuncGroup_0.func_1143(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1145:
          return Wat2WasmModuleMachineFuncGroup_0.func_1145(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1146:
          return Wat2WasmModuleMachineFuncGroup_0.func_1146(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1147:
          return Wat2WasmModuleMachineFuncGroup_0.func_1147(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1148:
          return Wat2WasmModuleMachineFuncGroup_0.func_1148(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1149:
          return Wat2WasmModuleMachineFuncGroup_0.func_1149(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1150:
          return Wat2WasmModuleMachineFuncGroup_0.func_1150(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1154:
          return Wat2WasmModuleMachineFuncGroup_0.func_1154(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1155:
          return Wat2WasmModuleMachineFuncGroup_0.func_1155(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1158:
          return Wat2WasmModuleMachineFuncGroup_0.func_1158(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1159:
          return Wat2WasmModuleMachineFuncGroup_0.func_1159(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1160:
          return Wat2WasmModuleMachineFuncGroup_0.func_1160(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1161:
          return Wat2WasmModuleMachineFuncGroup_0.func_1161(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1165:
          return Wat2WasmModuleMachineFuncGroup_0.func_1165(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1166:
          return Wat2WasmModuleMachineFuncGroup_0.func_1166(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1167:
          return Wat2WasmModuleMachineFuncGroup_0.func_1167(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1168:
          return Wat2WasmModuleMachineFuncGroup_0.func_1168(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1171:
          return Wat2WasmModuleMachineFuncGroup_0.func_1171(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1172:
          return Wat2WasmModuleMachineFuncGroup_0.func_1172(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1173:
          return Wat2WasmModuleMachineFuncGroup_0.func_1173(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1174:
          return Wat2WasmModuleMachineFuncGroup_0.func_1174(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1175:
          return Wat2WasmModuleMachineFuncGroup_0.func_1175(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1176:
          return Wat2WasmModuleMachineFuncGroup_0.func_1176(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1177:
          return Wat2WasmModuleMachineFuncGroup_0.func_1177(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1178:
          return Wat2WasmModuleMachineFuncGroup_0.func_1178(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1179:
          return Wat2WasmModuleMachineFuncGroup_0.func_1179(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1180:
          return Wat2WasmModuleMachineFuncGroup_0.func_1180(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1181:
          return Wat2WasmModuleMachineFuncGroup_0.func_1181(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1182:
          return Wat2WasmModuleMachineFuncGroup_0.func_1182(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1183:
          return Wat2WasmModuleMachineFuncGroup_0.func_1183(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1184:
          return Wat2WasmModuleMachineFuncGroup_0.func_1184(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1186:
          return Wat2WasmModuleMachineFuncGroup_0.func_1186(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1187:
          return Wat2WasmModuleMachineFuncGroup_0.func_1187(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1190:
          return Wat2WasmModuleMachineFuncGroup_0.func_1190(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1191:
          return Wat2WasmModuleMachineFuncGroup_0.func_1191(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1194:
          return Wat2WasmModuleMachineFuncGroup_0.func_1194(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1195:
          return Wat2WasmModuleMachineFuncGroup_0.func_1195(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1197:
          return Wat2WasmModuleMachineFuncGroup_0.func_1197(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1198:
          return Wat2WasmModuleMachineFuncGroup_0.func_1198(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1200:
          return Wat2WasmModuleMachineFuncGroup_0.func_1200(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1201:
          return Wat2WasmModuleMachineFuncGroup_0.func_1201(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1203:
          return Wat2WasmModuleMachineFuncGroup_0.func_1203(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1204:
          return Wat2WasmModuleMachineFuncGroup_0.func_1204(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1206:
          return Wat2WasmModuleMachineFuncGroup_0.func_1206(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1207:
          return Wat2WasmModuleMachineFuncGroup_0.func_1207(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1209:
          return Wat2WasmModuleMachineFuncGroup_0.func_1209(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1212:
          return Wat2WasmModuleMachineFuncGroup_0.func_1212(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1213:
          return Wat2WasmModuleMachineFuncGroup_0.func_1213(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1214:
          return Wat2WasmModuleMachineFuncGroup_0.func_1214(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1219:
          return Wat2WasmModuleMachineFuncGroup_0.func_1219(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1220:
          return Wat2WasmModuleMachineFuncGroup_0.func_1220(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1221:
          return Wat2WasmModuleMachineFuncGroup_0.func_1221(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1222:
          return Wat2WasmModuleMachineFuncGroup_0.func_1222(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1223:
          return Wat2WasmModuleMachineFuncGroup_0.func_1223(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1224:
          return Wat2WasmModuleMachineFuncGroup_0.func_1224(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1225:
          return Wat2WasmModuleMachineFuncGroup_0.func_1225(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1226:
          return Wat2WasmModuleMachineFuncGroup_0.func_1226(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1227:
          return Wat2WasmModuleMachineFuncGroup_0.func_1227(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1228:
          return Wat2WasmModuleMachineFuncGroup_0.func_1228(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1229:
          return Wat2WasmModuleMachineFuncGroup_0.func_1229(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1232:
          return Wat2WasmModuleMachineFuncGroup_0.func_1232(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1233:
          return Wat2WasmModuleMachineFuncGroup_0.func_1233(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1235:
          return Wat2WasmModuleMachineFuncGroup_0.func_1235(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1240:
          return Wat2WasmModuleMachineFuncGroup_0.func_1240(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1242:
          return Wat2WasmModuleMachineFuncGroup_0.func_1242(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1243:
          return Wat2WasmModuleMachineFuncGroup_0.func_1243(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1249:
          return Wat2WasmModuleMachineFuncGroup_0.func_1249(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1254:
          return Wat2WasmModuleMachineFuncGroup_0.func_1254(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1255:
          return Wat2WasmModuleMachineFuncGroup_0.func_1255(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1257:
          return Wat2WasmModuleMachineFuncGroup_0.func_1257(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1258:
          return Wat2WasmModuleMachineFuncGroup_0.func_1258(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1297:
          return Wat2WasmModuleMachineFuncGroup_0.func_1297(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1298:
          return Wat2WasmModuleMachineFuncGroup_0.func_1298(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1318:
          return Wat2WasmModuleMachineFuncGroup_0.func_1318(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1319:
          return Wat2WasmModuleMachineFuncGroup_0.func_1319(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1321:
          return Wat2WasmModuleMachineFuncGroup_0.func_1321(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1322:
          return Wat2WasmModuleMachineFuncGroup_0.func_1322(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1323:
          return Wat2WasmModuleMachineFuncGroup_0.func_1323(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1324:
          return Wat2WasmModuleMachineFuncGroup_0.func_1324(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1325:
          return Wat2WasmModuleMachineFuncGroup_0.func_1325(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1326:
          return Wat2WasmModuleMachineFuncGroup_0.func_1326(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1327:
          return Wat2WasmModuleMachineFuncGroup_0.func_1327(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1328:
          return Wat2WasmModuleMachineFuncGroup_0.func_1328(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1329:
          return Wat2WasmModuleMachineFuncGroup_0.func_1329(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1330:
          return Wat2WasmModuleMachineFuncGroup_0.func_1330(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1331:
          return Wat2WasmModuleMachineFuncGroup_0.func_1331(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1332:
          return Wat2WasmModuleMachineFuncGroup_0.func_1332(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1333:
          return Wat2WasmModuleMachineFuncGroup_0.func_1333(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1334:
          return Wat2WasmModuleMachineFuncGroup_0.func_1334(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1335:
          return Wat2WasmModuleMachineFuncGroup_0.func_1335(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1336:
          return Wat2WasmModuleMachineFuncGroup_0.func_1336(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1337:
          return Wat2WasmModuleMachineFuncGroup_0.func_1337(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1338:
          return Wat2WasmModuleMachineFuncGroup_0.func_1338(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1339:
          return Wat2WasmModuleMachineFuncGroup_0.func_1339(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1340:
          return Wat2WasmModuleMachineFuncGroup_0.func_1340(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1341:
          return Wat2WasmModuleMachineFuncGroup_0.func_1341(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1342:
          return Wat2WasmModuleMachineFuncGroup_0.func_1342(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1343:
          return Wat2WasmModuleMachineFuncGroup_0.func_1343(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1344:
          return Wat2WasmModuleMachineFuncGroup_0.func_1344(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1345:
          return Wat2WasmModuleMachineFuncGroup_0.func_1345(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1346:
          return Wat2WasmModuleMachineFuncGroup_0.func_1346(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1347:
          return Wat2WasmModuleMachineFuncGroup_0.func_1347(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1348:
          return Wat2WasmModuleMachineFuncGroup_0.func_1348(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1349:
          return Wat2WasmModuleMachineFuncGroup_0.func_1349(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1350:
          return Wat2WasmModuleMachineFuncGroup_0.func_1350(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1351:
          return Wat2WasmModuleMachineFuncGroup_0.func_1351(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1352:
          return Wat2WasmModuleMachineFuncGroup_0.func_1352(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1353:
          return Wat2WasmModuleMachineFuncGroup_0.func_1353(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1354:
          return Wat2WasmModuleMachineFuncGroup_0.func_1354(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1355:
          return Wat2WasmModuleMachineFuncGroup_0.func_1355(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1356:
          return Wat2WasmModuleMachineFuncGroup_0.func_1356(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1357:
          return Wat2WasmModuleMachineFuncGroup_0.func_1357(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1358:
          return Wat2WasmModuleMachineFuncGroup_0.func_1358(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1359:
          return Wat2WasmModuleMachineFuncGroup_0.func_1359(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1360:
          return Wat2WasmModuleMachineFuncGroup_0.func_1360(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1361:
          return Wat2WasmModuleMachineFuncGroup_0.func_1361(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1362:
          return Wat2WasmModuleMachineFuncGroup_0.func_1362(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1363:
          return Wat2WasmModuleMachineFuncGroup_0.func_1363(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1364:
          return Wat2WasmModuleMachineFuncGroup_0.func_1364(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1365:
          return Wat2WasmModuleMachineFuncGroup_0.func_1365(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1366:
          return Wat2WasmModuleMachineFuncGroup_0.func_1366(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1367:
          return Wat2WasmModuleMachineFuncGroup_0.func_1367(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1368:
          return Wat2WasmModuleMachineFuncGroup_0.func_1368(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1369:
          return Wat2WasmModuleMachineFuncGroup_0.func_1369(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1370:
          return Wat2WasmModuleMachineFuncGroup_0.func_1370(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1371:
          return Wat2WasmModuleMachineFuncGroup_0.func_1371(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1373:
          return Wat2WasmModuleMachineFuncGroup_0.func_1373(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1374:
          return Wat2WasmModuleMachineFuncGroup_0.func_1374(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1375:
          return Wat2WasmModuleMachineFuncGroup_0.func_1375(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1376:
          return Wat2WasmModuleMachineFuncGroup_0.func_1376(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1377:
          return Wat2WasmModuleMachineFuncGroup_0.func_1377(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1378:
          return Wat2WasmModuleMachineFuncGroup_0.func_1378(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1379:
          return Wat2WasmModuleMachineFuncGroup_0.func_1379(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1380:
          return Wat2WasmModuleMachineFuncGroup_0.func_1380(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1381:
          return Wat2WasmModuleMachineFuncGroup_0.func_1381(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1382:
          return Wat2WasmModuleMachineFuncGroup_0.func_1382(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1383:
          return Wat2WasmModuleMachineFuncGroup_0.func_1383(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1384:
          return Wat2WasmModuleMachineFuncGroup_0.func_1384(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1385:
          return Wat2WasmModuleMachineFuncGroup_0.func_1385(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1386:
          return Wat2WasmModuleMachineFuncGroup_0.func_1386(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1387:
          return Wat2WasmModuleMachineFuncGroup_0.func_1387(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1388:
          return Wat2WasmModuleMachineFuncGroup_0.func_1388(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1389:
          return Wat2WasmModuleMachineFuncGroup_0.func_1389(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1390:
          return Wat2WasmModuleMachineFuncGroup_0.func_1390(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1391:
          return Wat2WasmModuleMachineFuncGroup_0.func_1391(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1407:
          return Wat2WasmModuleMachineFuncGroup_0.func_1407(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1578:
          return Wat2WasmModuleMachineFuncGroup_0.func_1578(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1581:
          return Wat2WasmModuleMachineFuncGroup_0.func_1581(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1583:
          return Wat2WasmModuleMachineFuncGroup_0.func_1583(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1586:
          return Wat2WasmModuleMachineFuncGroup_0.func_1586(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1587:
          return Wat2WasmModuleMachineFuncGroup_0.func_1587(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1592:
          return Wat2WasmModuleMachineFuncGroup_0.func_1592(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1603:
          return Wat2WasmModuleMachineFuncGroup_0.func_1603(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1613:
          return Wat2WasmModuleMachineFuncGroup_0.func_1613(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1635:
          return Wat2WasmModuleMachineFuncGroup_0.func_1635(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1650:
          return Wat2WasmModuleMachineFuncGroup_0.func_1650(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1651:
          return Wat2WasmModuleMachineFuncGroup_0.func_1651(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1657:
          return Wat2WasmModuleMachineFuncGroup_0.func_1657(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1662:
          return Wat2WasmModuleMachineFuncGroup_0.func_1662(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1680:
          return Wat2WasmModuleMachineFuncGroup_0.func_1680(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1682:
          return Wat2WasmModuleMachineFuncGroup_0.func_1682(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1692:
          return Wat2WasmModuleMachineFuncGroup_0.func_1692(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1693:
          return Wat2WasmModuleMachineFuncGroup_0.func_1693(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1699:
          return Wat2WasmModuleMachineFuncGroup_0.func_1699(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1700:
          return Wat2WasmModuleMachineFuncGroup_0.func_1700(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1706:
          return Wat2WasmModuleMachineFuncGroup_0.func_1706(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1707:
          return Wat2WasmModuleMachineFuncGroup_0.func_1707(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1724:
          return Wat2WasmModuleMachineFuncGroup_0.func_1724(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1726:
          return Wat2WasmModuleMachineFuncGroup_0.func_1726(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1727:
          return Wat2WasmModuleMachineFuncGroup_0.func_1727(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1728:
          return Wat2WasmModuleMachineFuncGroup_0.func_1728(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1729:
          return Wat2WasmModuleMachineFuncGroup_0.func_1729(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1730:
          return Wat2WasmModuleMachineFuncGroup_0.func_1730(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1731:
          return Wat2WasmModuleMachineFuncGroup_0.func_1731(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1732:
          return Wat2WasmModuleMachineFuncGroup_0.func_1732(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1733:
          return Wat2WasmModuleMachineFuncGroup_0.func_1733(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1734:
          return Wat2WasmModuleMachineFuncGroup_0.func_1734(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1735:
          return Wat2WasmModuleMachineFuncGroup_0.func_1735(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1736:
          return Wat2WasmModuleMachineFuncGroup_0.func_1736(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1738:
          return Wat2WasmModuleMachineFuncGroup_0.func_1738(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1739:
          return Wat2WasmModuleMachineFuncGroup_0.func_1739(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1754:
          return Wat2WasmModuleMachineFuncGroup_0.func_1754(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1762:
          return Wat2WasmModuleMachineFuncGroup_0.func_1762(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1774:
          return Wat2WasmModuleMachineFuncGroup_0.func_1774(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1778:
          return Wat2WasmModuleMachineFuncGroup_0.func_1778(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1779:
          return Wat2WasmModuleMachineFuncGroup_0.func_1779(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1786:
          return Wat2WasmModuleMachineFuncGroup_0.func_1786(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1788:
          return Wat2WasmModuleMachineFuncGroup_0.func_1788(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1789:
          return Wat2WasmModuleMachineFuncGroup_0.func_1789(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1792:
          return Wat2WasmModuleMachineFuncGroup_0.func_1792(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1793:
          return Wat2WasmModuleMachineFuncGroup_0.func_1793(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1794:
          return Wat2WasmModuleMachineFuncGroup_0.func_1794(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1795:
          return Wat2WasmModuleMachineFuncGroup_0.func_1795(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1797:
          return Wat2WasmModuleMachineFuncGroup_0.func_1797(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1798:
          return Wat2WasmModuleMachineFuncGroup_0.func_1798(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1799:
          return Wat2WasmModuleMachineFuncGroup_0.func_1799(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1812:
          return Wat2WasmModuleMachineFuncGroup_0.func_1812(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1813:
          return Wat2WasmModuleMachineFuncGroup_0.func_1813(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1814:
          return Wat2WasmModuleMachineFuncGroup_0.func_1814(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1828:
          return Wat2WasmModuleMachineFuncGroup_0.func_1828(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1829:
          return Wat2WasmModuleMachineFuncGroup_0.func_1829(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1845:
          return Wat2WasmModuleMachineFuncGroup_0.func_1845(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1846:
          return Wat2WasmModuleMachineFuncGroup_0.func_1846(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1848:
          return Wat2WasmModuleMachineFuncGroup_0.func_1848(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1860:
          return Wat2WasmModuleMachineFuncGroup_0.func_1860(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1861:
          return Wat2WasmModuleMachineFuncGroup_0.func_1861(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1863:
          return Wat2WasmModuleMachineFuncGroup_0.func_1863(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1868:
          return Wat2WasmModuleMachineFuncGroup_0.func_1868(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1893:
          return Wat2WasmModuleMachineFuncGroup_0.func_1893(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1894:
          return Wat2WasmModuleMachineFuncGroup_0.func_1894(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1895:
          return Wat2WasmModuleMachineFuncGroup_0.func_1895(paramInt1, paramInt2, paramMemory, paramInstance);
        case 1899:
          return Wat2WasmModuleMachineFuncGroup_0.func_1899(paramInt1, paramInt2, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2 }, 5, i, instance)[0];
  }
  
  public static int call_indirect_6(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt5);
    int i = tableInstance.requiredRef(paramInt4);
    Instance instance = tableInstance.instance(paramInt4);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 8:
          return Wat2WasmModuleMachineFuncGroup_0.func_8(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 40:
          return Wat2WasmModuleMachineFuncGroup_0.func_40(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 83:
          return Wat2WasmModuleMachineFuncGroup_0.func_83(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 86:
          return Wat2WasmModuleMachineFuncGroup_0.func_86(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 155:
          return Wat2WasmModuleMachineFuncGroup_0.func_155(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 156:
          return Wat2WasmModuleMachineFuncGroup_0.func_156(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 165:
          return Wat2WasmModuleMachineFuncGroup_0.func_165(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 172:
          return Wat2WasmModuleMachineFuncGroup_0.func_172(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 177:
          return Wat2WasmModuleMachineFuncGroup_0.func_177(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 178:
          return Wat2WasmModuleMachineFuncGroup_0.func_178(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 179:
          return Wat2WasmModuleMachineFuncGroup_0.func_179(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 180:
          return Wat2WasmModuleMachineFuncGroup_0.func_180(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 183:
          return Wat2WasmModuleMachineFuncGroup_0.func_183(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 184:
          return Wat2WasmModuleMachineFuncGroup_0.func_184(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 185:
          return Wat2WasmModuleMachineFuncGroup_0.func_185(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 188:
          return Wat2WasmModuleMachineFuncGroup_0.func_188(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 190:
          return Wat2WasmModuleMachineFuncGroup_0.func_190(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 198:
          return Wat2WasmModuleMachineFuncGroup_0.func_198(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 247:
          return Wat2WasmModuleMachineFuncGroup_0.func_247(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 255:
          return Wat2WasmModuleMachineFuncGroup_0.func_255(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 257:
          return Wat2WasmModuleMachineFuncGroup_0.func_257(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 262:
          return Wat2WasmModuleMachineFuncGroup_0.func_262(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 263:
          return Wat2WasmModuleMachineFuncGroup_0.func_263(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 264:
          return Wat2WasmModuleMachineFuncGroup_0.func_264(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 265:
          return Wat2WasmModuleMachineFuncGroup_0.func_265(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 266:
          return Wat2WasmModuleMachineFuncGroup_0.func_266(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 268:
          return Wat2WasmModuleMachineFuncGroup_0.func_268(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 270:
          return Wat2WasmModuleMachineFuncGroup_0.func_270(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 276:
          return Wat2WasmModuleMachineFuncGroup_0.func_276(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 279:
          return Wat2WasmModuleMachineFuncGroup_0.func_279(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 280:
          return Wat2WasmModuleMachineFuncGroup_0.func_280(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 290:
          return Wat2WasmModuleMachineFuncGroup_0.func_290(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 293:
          return Wat2WasmModuleMachineFuncGroup_0.func_293(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 297:
          return Wat2WasmModuleMachineFuncGroup_0.func_297(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 298:
          return Wat2WasmModuleMachineFuncGroup_0.func_298(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 302:
          return Wat2WasmModuleMachineFuncGroup_0.func_302(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 304:
          return Wat2WasmModuleMachineFuncGroup_0.func_304(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 306:
          return Wat2WasmModuleMachineFuncGroup_0.func_306(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 307:
          return Wat2WasmModuleMachineFuncGroup_0.func_307(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 308:
          return Wat2WasmModuleMachineFuncGroup_0.func_308(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 309:
          return Wat2WasmModuleMachineFuncGroup_0.func_309(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 311:
          return Wat2WasmModuleMachineFuncGroup_0.func_311(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 312:
          return Wat2WasmModuleMachineFuncGroup_0.func_312(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 320:
          return Wat2WasmModuleMachineFuncGroup_0.func_320(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 321:
          return Wat2WasmModuleMachineFuncGroup_0.func_321(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 323:
          return Wat2WasmModuleMachineFuncGroup_0.func_323(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 324:
          return Wat2WasmModuleMachineFuncGroup_0.func_324(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 330:
          return Wat2WasmModuleMachineFuncGroup_0.func_330(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 335:
          return Wat2WasmModuleMachineFuncGroup_0.func_335(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 340:
          return Wat2WasmModuleMachineFuncGroup_0.func_340(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 341:
          return Wat2WasmModuleMachineFuncGroup_0.func_341(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 342:
          return Wat2WasmModuleMachineFuncGroup_0.func_342(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 350:
          return Wat2WasmModuleMachineFuncGroup_0.func_350(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 354:
          return Wat2WasmModuleMachineFuncGroup_0.func_354(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 355:
          return Wat2WasmModuleMachineFuncGroup_0.func_355(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 357:
          return Wat2WasmModuleMachineFuncGroup_0.func_357(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 359:
          return Wat2WasmModuleMachineFuncGroup_0.func_359(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 361:
          return Wat2WasmModuleMachineFuncGroup_0.func_361(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 365:
          return Wat2WasmModuleMachineFuncGroup_0.func_365(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 368:
          return Wat2WasmModuleMachineFuncGroup_0.func_368(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 370:
          return Wat2WasmModuleMachineFuncGroup_0.func_370(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 373:
          return Wat2WasmModuleMachineFuncGroup_0.func_373(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 380:
          return Wat2WasmModuleMachineFuncGroup_0.func_380(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 381:
          return Wat2WasmModuleMachineFuncGroup_0.func_381(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 382:
          return Wat2WasmModuleMachineFuncGroup_0.func_382(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 383:
          return Wat2WasmModuleMachineFuncGroup_0.func_383(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 385:
          return Wat2WasmModuleMachineFuncGroup_0.func_385(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 387:
          return Wat2WasmModuleMachineFuncGroup_0.func_387(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 389:
          return Wat2WasmModuleMachineFuncGroup_0.func_389(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 391:
          return Wat2WasmModuleMachineFuncGroup_0.func_391(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 392:
          return Wat2WasmModuleMachineFuncGroup_0.func_392(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 393:
          return Wat2WasmModuleMachineFuncGroup_0.func_393(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 394:
          return Wat2WasmModuleMachineFuncGroup_0.func_394(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 395:
          return Wat2WasmModuleMachineFuncGroup_0.func_395(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 397:
          return Wat2WasmModuleMachineFuncGroup_0.func_397(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 400:
          return Wat2WasmModuleMachineFuncGroup_0.func_400(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 401:
          return Wat2WasmModuleMachineFuncGroup_0.func_401(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 402:
          return Wat2WasmModuleMachineFuncGroup_0.func_402(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 406:
          return Wat2WasmModuleMachineFuncGroup_0.func_406(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 407:
          return Wat2WasmModuleMachineFuncGroup_0.func_407(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 408:
          return Wat2WasmModuleMachineFuncGroup_0.func_408(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 409:
          return Wat2WasmModuleMachineFuncGroup_0.func_409(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 411:
          return Wat2WasmModuleMachineFuncGroup_0.func_411(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 412:
          return Wat2WasmModuleMachineFuncGroup_0.func_412(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 414:
          return Wat2WasmModuleMachineFuncGroup_0.func_414(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 416:
          return Wat2WasmModuleMachineFuncGroup_0.func_416(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 419:
          return Wat2WasmModuleMachineFuncGroup_0.func_419(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 420:
          return Wat2WasmModuleMachineFuncGroup_0.func_420(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 421:
          return Wat2WasmModuleMachineFuncGroup_0.func_421(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 431:
          return Wat2WasmModuleMachineFuncGroup_0.func_431(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 432:
          return Wat2WasmModuleMachineFuncGroup_0.func_432(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 433:
          return Wat2WasmModuleMachineFuncGroup_0.func_433(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 435:
          return Wat2WasmModuleMachineFuncGroup_0.func_435(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 436:
          return Wat2WasmModuleMachineFuncGroup_0.func_436(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 437:
          return Wat2WasmModuleMachineFuncGroup_0.func_437(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 438:
          return Wat2WasmModuleMachineFuncGroup_0.func_438(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 440:
          return Wat2WasmModuleMachineFuncGroup_0.func_440(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 441:
          return Wat2WasmModuleMachineFuncGroup_0.func_441(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 442:
          return Wat2WasmModuleMachineFuncGroup_0.func_442(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 443:
          return Wat2WasmModuleMachineFuncGroup_0.func_443(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 444:
          return Wat2WasmModuleMachineFuncGroup_0.func_444(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 457:
          return Wat2WasmModuleMachineFuncGroup_0.func_457(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 514:
          return Wat2WasmModuleMachineFuncGroup_0.func_514(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 539:
          return Wat2WasmModuleMachineFuncGroup_0.func_539(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 547:
          return Wat2WasmModuleMachineFuncGroup_0.func_547(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 553:
          return Wat2WasmModuleMachineFuncGroup_0.func_553(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 555:
          return Wat2WasmModuleMachineFuncGroup_0.func_555(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 572:
          return Wat2WasmModuleMachineFuncGroup_0.func_572(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 582:
          return Wat2WasmModuleMachineFuncGroup_0.func_582(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 593:
          return Wat2WasmModuleMachineFuncGroup_0.func_593(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 606:
          return Wat2WasmModuleMachineFuncGroup_0.func_606(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 608:
          return Wat2WasmModuleMachineFuncGroup_0.func_608(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 610:
          return Wat2WasmModuleMachineFuncGroup_0.func_610(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 612:
          return Wat2WasmModuleMachineFuncGroup_0.func_612(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 617:
          return Wat2WasmModuleMachineFuncGroup_0.func_617(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 624:
          return Wat2WasmModuleMachineFuncGroup_0.func_624(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 626:
          return Wat2WasmModuleMachineFuncGroup_0.func_626(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 628:
          return Wat2WasmModuleMachineFuncGroup_0.func_628(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 636:
          return Wat2WasmModuleMachineFuncGroup_0.func_636(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 638:
          return Wat2WasmModuleMachineFuncGroup_0.func_638(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 639:
          return Wat2WasmModuleMachineFuncGroup_0.func_639(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 652:
          return Wat2WasmModuleMachineFuncGroup_0.func_652(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 686:
          return Wat2WasmModuleMachineFuncGroup_0.func_686(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 702:
          return Wat2WasmModuleMachineFuncGroup_0.func_702(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 706:
          return Wat2WasmModuleMachineFuncGroup_0.func_706(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 708:
          return Wat2WasmModuleMachineFuncGroup_0.func_708(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 710:
          return Wat2WasmModuleMachineFuncGroup_0.func_710(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 722:
          return Wat2WasmModuleMachineFuncGroup_0.func_722(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 739:
          return Wat2WasmModuleMachineFuncGroup_0.func_739(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 740:
          return Wat2WasmModuleMachineFuncGroup_0.func_740(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 741:
          return Wat2WasmModuleMachineFuncGroup_0.func_741(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 757:
          return Wat2WasmModuleMachineFuncGroup_0.func_757(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 780:
          return Wat2WasmModuleMachineFuncGroup_0.func_780(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 783:
          return Wat2WasmModuleMachineFuncGroup_0.func_783(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 788:
          return Wat2WasmModuleMachineFuncGroup_0.func_788(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 790:
          return Wat2WasmModuleMachineFuncGroup_0.func_790(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 803:
          return Wat2WasmModuleMachineFuncGroup_0.func_803(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 804:
          return Wat2WasmModuleMachineFuncGroup_0.func_804(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 805:
          return Wat2WasmModuleMachineFuncGroup_0.func_805(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 806:
          return Wat2WasmModuleMachineFuncGroup_0.func_806(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 807:
          return Wat2WasmModuleMachineFuncGroup_0.func_807(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 808:
          return Wat2WasmModuleMachineFuncGroup_0.func_808(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 811:
          return Wat2WasmModuleMachineFuncGroup_0.func_811(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 812:
          return Wat2WasmModuleMachineFuncGroup_0.func_812(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 813:
          return Wat2WasmModuleMachineFuncGroup_0.func_813(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 814:
          return Wat2WasmModuleMachineFuncGroup_0.func_814(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 815:
          return Wat2WasmModuleMachineFuncGroup_0.func_815(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 817:
          return Wat2WasmModuleMachineFuncGroup_0.func_817(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 818:
          return Wat2WasmModuleMachineFuncGroup_0.func_818(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 826:
          return Wat2WasmModuleMachineFuncGroup_0.func_826(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 828:
          return Wat2WasmModuleMachineFuncGroup_0.func_828(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 856:
          return Wat2WasmModuleMachineFuncGroup_0.func_856(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 870:
          return Wat2WasmModuleMachineFuncGroup_0.func_870(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 897:
          return Wat2WasmModuleMachineFuncGroup_0.func_897(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 906:
          return Wat2WasmModuleMachineFuncGroup_0.func_906(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 907:
          return Wat2WasmModuleMachineFuncGroup_0.func_907(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 931:
          return Wat2WasmModuleMachineFuncGroup_0.func_931(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 955:
          return Wat2WasmModuleMachineFuncGroup_0.func_955(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 959:
          return Wat2WasmModuleMachineFuncGroup_0.func_959(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 961:
          return Wat2WasmModuleMachineFuncGroup_0.func_961(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 963:
          return Wat2WasmModuleMachineFuncGroup_0.func_963(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 976:
          return Wat2WasmModuleMachineFuncGroup_0.func_976(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 977:
          return Wat2WasmModuleMachineFuncGroup_0.func_977(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 983:
          return Wat2WasmModuleMachineFuncGroup_0.func_983(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 991:
          return Wat2WasmModuleMachineFuncGroup_0.func_991(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1001:
          return Wat2WasmModuleMachineFuncGroup_0.func_1001(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1002:
          return Wat2WasmModuleMachineFuncGroup_0.func_1002(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1004:
          return Wat2WasmModuleMachineFuncGroup_0.func_1004(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1006:
          return Wat2WasmModuleMachineFuncGroup_0.func_1006(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1026:
          return Wat2WasmModuleMachineFuncGroup_0.func_1026(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1030:
          return Wat2WasmModuleMachineFuncGroup_0.func_1030(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1032:
          return Wat2WasmModuleMachineFuncGroup_0.func_1032(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1036:
          return Wat2WasmModuleMachineFuncGroup_0.func_1036(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1037:
          return Wat2WasmModuleMachineFuncGroup_0.func_1037(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1040:
          return Wat2WasmModuleMachineFuncGroup_0.func_1040(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1048:
          return Wat2WasmModuleMachineFuncGroup_0.func_1048(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1054:
          return Wat2WasmModuleMachineFuncGroup_0.func_1054(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1071:
          return Wat2WasmModuleMachineFuncGroup_0.func_1071(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1074:
          return Wat2WasmModuleMachineFuncGroup_0.func_1074(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1078:
          return Wat2WasmModuleMachineFuncGroup_0.func_1078(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1080:
          return Wat2WasmModuleMachineFuncGroup_0.func_1080(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1082:
          return Wat2WasmModuleMachineFuncGroup_0.func_1082(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1144:
          return Wat2WasmModuleMachineFuncGroup_0.func_1144(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1163:
          return Wat2WasmModuleMachineFuncGroup_0.func_1163(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1164:
          return Wat2WasmModuleMachineFuncGroup_0.func_1164(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1170:
          return Wat2WasmModuleMachineFuncGroup_0.func_1170(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1192:
          return Wat2WasmModuleMachineFuncGroup_0.func_1192(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1196:
          return Wat2WasmModuleMachineFuncGroup_0.func_1196(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1199:
          return Wat2WasmModuleMachineFuncGroup_0.func_1199(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1202:
          return Wat2WasmModuleMachineFuncGroup_0.func_1202(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1205:
          return Wat2WasmModuleMachineFuncGroup_0.func_1205(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1230:
          return Wat2WasmModuleMachineFuncGroup_0.func_1230(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1236:
          return Wat2WasmModuleMachineFuncGroup_0.func_1236(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1259:
          return Wat2WasmModuleMachineFuncGroup_0.func_1259(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1260:
          return Wat2WasmModuleMachineFuncGroup_0.func_1260(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1261:
          return Wat2WasmModuleMachineFuncGroup_0.func_1261(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1263:
          return Wat2WasmModuleMachineFuncGroup_0.func_1263(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1265:
          return Wat2WasmModuleMachineFuncGroup_0.func_1265(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1267:
          return Wat2WasmModuleMachineFuncGroup_0.func_1267(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1268:
          return Wat2WasmModuleMachineFuncGroup_0.func_1268(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1269:
          return Wat2WasmModuleMachineFuncGroup_0.func_1269(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1270:
          return Wat2WasmModuleMachineFuncGroup_0.func_1270(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1271:
          return Wat2WasmModuleMachineFuncGroup_0.func_1271(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1274:
          return Wat2WasmModuleMachineFuncGroup_0.func_1274(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1275:
          return Wat2WasmModuleMachineFuncGroup_0.func_1275(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1276:
          return Wat2WasmModuleMachineFuncGroup_0.func_1276(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1277:
          return Wat2WasmModuleMachineFuncGroup_0.func_1277(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1278:
          return Wat2WasmModuleMachineFuncGroup_0.func_1278(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1279:
          return Wat2WasmModuleMachineFuncGroup_0.func_1279(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1280:
          return Wat2WasmModuleMachineFuncGroup_0.func_1280(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1282:
          return Wat2WasmModuleMachineFuncGroup_0.func_1282(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1285:
          return Wat2WasmModuleMachineFuncGroup_0.func_1285(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1286:
          return Wat2WasmModuleMachineFuncGroup_0.func_1286(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1287:
          return Wat2WasmModuleMachineFuncGroup_0.func_1287(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1294:
          return Wat2WasmModuleMachineFuncGroup_0.func_1294(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1300:
          return Wat2WasmModuleMachineFuncGroup_0.func_1300(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1302:
          return Wat2WasmModuleMachineFuncGroup_0.func_1302(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1372:
          return Wat2WasmModuleMachineFuncGroup_0.func_1372(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1408:
          return Wat2WasmModuleMachineFuncGroup_0.func_1408(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1412:
          return Wat2WasmModuleMachineFuncGroup_0.func_1412(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1573:
          return Wat2WasmModuleMachineFuncGroup_0.func_1573(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1575:
          return Wat2WasmModuleMachineFuncGroup_0.func_1575(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1582:
          return Wat2WasmModuleMachineFuncGroup_0.func_1582(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1585:
          return Wat2WasmModuleMachineFuncGroup_0.func_1585(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1596:
          return Wat2WasmModuleMachineFuncGroup_0.func_1596(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1597:
          return Wat2WasmModuleMachineFuncGroup_0.func_1597(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1607:
          return Wat2WasmModuleMachineFuncGroup_0.func_1607(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1619:
          return Wat2WasmModuleMachineFuncGroup_0.func_1619(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1638:
          return Wat2WasmModuleMachineFuncGroup_0.func_1638(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1644:
          return Wat2WasmModuleMachineFuncGroup_0.func_1644(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1645:
          return Wat2WasmModuleMachineFuncGroup_0.func_1645(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1649:
          return Wat2WasmModuleMachineFuncGroup_0.func_1649(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1652:
          return Wat2WasmModuleMachineFuncGroup_0.func_1652(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1654:
          return Wat2WasmModuleMachineFuncGroup_0.func_1654(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1658:
          return Wat2WasmModuleMachineFuncGroup_0.func_1658(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1659:
          return Wat2WasmModuleMachineFuncGroup_0.func_1659(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1661:
          return Wat2WasmModuleMachineFuncGroup_0.func_1661(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1663:
          return Wat2WasmModuleMachineFuncGroup_0.func_1663(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1667:
          return Wat2WasmModuleMachineFuncGroup_0.func_1667(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1671:
          return Wat2WasmModuleMachineFuncGroup_0.func_1671(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1685:
          return Wat2WasmModuleMachineFuncGroup_0.func_1685(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1698:
          return Wat2WasmModuleMachineFuncGroup_0.func_1698(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1702:
          return Wat2WasmModuleMachineFuncGroup_0.func_1702(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1705:
          return Wat2WasmModuleMachineFuncGroup_0.func_1705(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1714:
          return Wat2WasmModuleMachineFuncGroup_0.func_1714(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1737:
          return Wat2WasmModuleMachineFuncGroup_0.func_1737(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1752:
          return Wat2WasmModuleMachineFuncGroup_0.func_1752(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1753:
          return Wat2WasmModuleMachineFuncGroup_0.func_1753(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1756:
          return Wat2WasmModuleMachineFuncGroup_0.func_1756(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1800:
          return Wat2WasmModuleMachineFuncGroup_0.func_1800(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1809:
          return Wat2WasmModuleMachineFuncGroup_0.func_1809(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1816:
          return Wat2WasmModuleMachineFuncGroup_0.func_1816(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1825:
          return Wat2WasmModuleMachineFuncGroup_0.func_1825(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1836:
          return Wat2WasmModuleMachineFuncGroup_0.func_1836(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1839:
          return Wat2WasmModuleMachineFuncGroup_0.func_1839(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1840:
          return Wat2WasmModuleMachineFuncGroup_0.func_1840(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1841:
          return Wat2WasmModuleMachineFuncGroup_0.func_1841(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1842:
          return Wat2WasmModuleMachineFuncGroup_0.func_1842(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1843:
          return Wat2WasmModuleMachineFuncGroup_0.func_1843(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1847:
          return Wat2WasmModuleMachineFuncGroup_0.func_1847(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1852:
          return Wat2WasmModuleMachineFuncGroup_0.func_1852(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1855:
          return Wat2WasmModuleMachineFuncGroup_0.func_1855(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1866:
          return Wat2WasmModuleMachineFuncGroup_0.func_1866(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1867:
          return Wat2WasmModuleMachineFuncGroup_0.func_1867(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1870:
          return Wat2WasmModuleMachineFuncGroup_0.func_1870(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1876:
          return Wat2WasmModuleMachineFuncGroup_0.func_1876(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1888:
          return Wat2WasmModuleMachineFuncGroup_0.func_1888(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1889:
          return Wat2WasmModuleMachineFuncGroup_0.func_1889(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1890:
          return Wat2WasmModuleMachineFuncGroup_0.func_1890(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1891:
          return Wat2WasmModuleMachineFuncGroup_0.func_1891(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1892:
          return Wat2WasmModuleMachineFuncGroup_0.func_1892(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1898:
          return Wat2WasmModuleMachineFuncGroup_0.func_1898(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3 }, 6, i, instance)[0];
  }
  
  public static int call_indirect_7(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt8);
    int i = tableInstance.requiredRef(paramInt7);
    Instance instance = tableInstance.instance(paramInt7);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 366:
          return Wat2WasmModuleMachineFuncGroup_0.func_366(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
        case 580:
          return Wat2WasmModuleMachineFuncGroup_0.func_580(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
        case 584:
          return Wat2WasmModuleMachineFuncGroup_0.func_584(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
        case 588:
          return Wat2WasmModuleMachineFuncGroup_0.func_588(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
        case 838:
          return Wat2WasmModuleMachineFuncGroup_0.func_838(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
        case 852:
          return Wat2WasmModuleMachineFuncGroup_0.func_852(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
        case 861:
          return Wat2WasmModuleMachineFuncGroup_0.func_861(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
        case 866:
          return Wat2WasmModuleMachineFuncGroup_0.func_866(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
        case 1237:
          return Wat2WasmModuleMachineFuncGroup_0.func_1237(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6 }, 7, i, instance)[0];
  }
  
  public static int call_indirect_8(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt7);
    int i = tableInstance.requiredRef(paramInt6);
    Instance instance = tableInstance.instance(paramInt6);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 12:
          return Wat2WasmModuleMachineFuncGroup_0.func_12(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 17:
          return Wat2WasmModuleMachineFuncGroup_0.func_17(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 81:
          return Wat2WasmModuleMachineFuncGroup_0.func_81(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 245:
          return Wat2WasmModuleMachineFuncGroup_0.func_245(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 259:
          return Wat2WasmModuleMachineFuncGroup_0.func_259(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 260:
          return Wat2WasmModuleMachineFuncGroup_0.func_260(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 349:
          return Wat2WasmModuleMachineFuncGroup_0.func_349(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 352:
          return Wat2WasmModuleMachineFuncGroup_0.func_352(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 356:
          return Wat2WasmModuleMachineFuncGroup_0.func_356(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 358:
          return Wat2WasmModuleMachineFuncGroup_0.func_358(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 583:
          return Wat2WasmModuleMachineFuncGroup_0.func_583(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 592:
          return Wat2WasmModuleMachineFuncGroup_0.func_592(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 622:
          return Wat2WasmModuleMachineFuncGroup_0.func_622(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 627:
          return Wat2WasmModuleMachineFuncGroup_0.func_627(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 630:
          return Wat2WasmModuleMachineFuncGroup_0.func_630(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 631:
          return Wat2WasmModuleMachineFuncGroup_0.func_631(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 633:
          return Wat2WasmModuleMachineFuncGroup_0.func_633(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 634:
          return Wat2WasmModuleMachineFuncGroup_0.func_634(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 792:
          return Wat2WasmModuleMachineFuncGroup_0.func_792(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 860:
          return Wat2WasmModuleMachineFuncGroup_0.func_860(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 890:
          return Wat2WasmModuleMachineFuncGroup_0.func_890(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 909:
          return Wat2WasmModuleMachineFuncGroup_0.func_909(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1041:
          return Wat2WasmModuleMachineFuncGroup_0.func_1041(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1044:
          return Wat2WasmModuleMachineFuncGroup_0.func_1044(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1063:
          return Wat2WasmModuleMachineFuncGroup_0.func_1063(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1064:
          return Wat2WasmModuleMachineFuncGroup_0.func_1064(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1066:
          return Wat2WasmModuleMachineFuncGroup_0.func_1066(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1067:
          return Wat2WasmModuleMachineFuncGroup_0.func_1067(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1308:
          return Wat2WasmModuleMachineFuncGroup_0.func_1308(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1577:
          return Wat2WasmModuleMachineFuncGroup_0.func_1577(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1653:
          return Wat2WasmModuleMachineFuncGroup_0.func_1653(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
        case 1871:
          return Wat2WasmModuleMachineFuncGroup_0.func_1871(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramInt5 }, 8, i, instance)[0];
  }
  
  public static int call_indirect_9(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt9);
    int i = tableInstance.requiredRef(paramInt8);
    Instance instance = tableInstance.instance(paramInt8);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 338:
          return Wat2WasmModuleMachineFuncGroup_0.func_338(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
        case 585:
          return Wat2WasmModuleMachineFuncGroup_0.func_585(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
        case 586:
          return Wat2WasmModuleMachineFuncGroup_0.func_586(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
        case 587:
          return Wat2WasmModuleMachineFuncGroup_0.func_587(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
        case 629:
          return Wat2WasmModuleMachineFuncGroup_0.func_629(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
        case 863:
          return Wat2WasmModuleMachineFuncGroup_0.func_863(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
        case 864:
          return Wat2WasmModuleMachineFuncGroup_0.func_864(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
        case 865:
          return Wat2WasmModuleMachineFuncGroup_0.func_865(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
        case 1062:
          return Wat2WasmModuleMachineFuncGroup_0.func_1062(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7 }, 9, i, instance)[0];
  }
  
  public static int call_indirect_10(int paramInt1, long paramLong, int paramInt2, int paramInt3, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt3);
    int i = tableInstance.requiredRef(paramInt2);
    Instance instance = tableInstance.instance(paramInt2);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 600:
          return Wat2WasmModuleMachineFuncGroup_0.func_600(paramInt1, paramLong, paramMemory, paramInstance);
        case 603:
          return Wat2WasmModuleMachineFuncGroup_0.func_603(paramInt1, paramLong, paramMemory, paramInstance);
        case 793:
          return Wat2WasmModuleMachineFuncGroup_0.func_793(paramInt1, paramLong, paramMemory, paramInstance);
        case 795:
          return Wat2WasmModuleMachineFuncGroup_0.func_795(paramInt1, paramLong, paramMemory, paramInstance);
        case 910:
          return Wat2WasmModuleMachineFuncGroup_0.func_910(paramInt1, paramLong, paramMemory, paramInstance);
        case 912:
          return Wat2WasmModuleMachineFuncGroup_0.func_912(paramInt1, paramLong, paramMemory, paramInstance);
        case 943:
          return Wat2WasmModuleMachineFuncGroup_0.func_943(paramInt1, paramLong, paramMemory, paramInstance);
        case 948:
          return Wat2WasmModuleMachineFuncGroup_0.func_948(paramInt1, paramLong, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramLong }, 10, i, instance)[0];
  }
  
  public static int call_indirect_11(int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt4);
    int i = tableInstance.requiredRef(paramInt3);
    Instance instance = tableInstance.instance(paramInt3);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 327:
          return Wat2WasmModuleMachineFuncGroup_0.func_327(paramInt1, paramInt2, paramLong, paramMemory, paramInstance);
        case 609:
          return Wat2WasmModuleMachineFuncGroup_0.func_609(paramInt1, paramInt2, paramLong, paramMemory, paramInstance);
        case 990:
          return Wat2WasmModuleMachineFuncGroup_0.func_990(paramInt1, paramInt2, paramLong, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramLong }, 11, i, instance)[0];
  }
  
  public static int call_indirect_12(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4, int paramInt5, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt5);
    int i = tableInstance.requiredRef(paramInt4);
    Instance instance = tableInstance.instance(paramInt4);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 328:
          return Wat2WasmModuleMachineFuncGroup_0.func_328(paramInt1, paramInt2, paramInt3, paramLong, paramMemory, paramInstance);
        case 329:
          return Wat2WasmModuleMachineFuncGroup_0.func_329(paramInt1, paramInt2, paramInt3, paramLong, paramMemory, paramInstance);
        case 425:
          return Wat2WasmModuleMachineFuncGroup_0.func_425(paramInt1, paramInt2, paramInt3, paramLong, paramMemory, paramInstance);
        case 613:
          return Wat2WasmModuleMachineFuncGroup_0.func_613(paramInt1, paramInt2, paramInt3, paramLong, paramMemory, paramInstance);
        case 640:
          return Wat2WasmModuleMachineFuncGroup_0.func_640(paramInt1, paramInt2, paramInt3, paramLong, paramMemory, paramInstance);
        case 1014:
          return Wat2WasmModuleMachineFuncGroup_0.func_1014(paramInt1, paramInt2, paramInt3, paramLong, paramMemory, paramInstance);
        case 1083:
          return Wat2WasmModuleMachineFuncGroup_0.func_1083(paramInt1, paramInt2, paramInt3, paramLong, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramLong }, 12, i, instance)[0];
  }
  
  public static int call_indirect_13(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4, int paramInt5, int paramInt6, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt6);
    int i = tableInstance.requiredRef(paramInt5);
    Instance instance = tableInstance.instance(paramInt5);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 635:
          return Wat2WasmModuleMachineFuncGroup_0.func_635(paramInt1, paramInt2, paramInt3, paramLong, paramInt4, paramMemory, paramInstance);
        case 1069:
          return Wat2WasmModuleMachineFuncGroup_0.func_1069(paramInt1, paramInt2, paramInt3, paramLong, paramInt4, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramLong, paramInt4 }, 13, i, instance)[0];
  }
  
  public static int call_indirect_14(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, int paramInt4, int paramInt5, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt5);
    int i = tableInstance.requiredRef(paramInt4);
    Instance instance = tableInstance.instance(paramInt4);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 677:
          return Wat2WasmModuleMachineFuncGroup_0.func_677(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 678:
          return Wat2WasmModuleMachineFuncGroup_0.func_678(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 679:
          return Wat2WasmModuleMachineFuncGroup_0.func_679(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 680:
          return Wat2WasmModuleMachineFuncGroup_0.func_680(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 681:
          return Wat2WasmModuleMachineFuncGroup_0.func_681(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 683:
          return Wat2WasmModuleMachineFuncGroup_0.func_683(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 698:
          return Wat2WasmModuleMachineFuncGroup_0.func_698(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 724:
          return Wat2WasmModuleMachineFuncGroup_0.func_724(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 725:
          return Wat2WasmModuleMachineFuncGroup_0.func_725(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 726:
          return Wat2WasmModuleMachineFuncGroup_0.func_726(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 916:
          return Wat2WasmModuleMachineFuncGroup_0.func_916(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 917:
          return Wat2WasmModuleMachineFuncGroup_0.func_917(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 918:
          return Wat2WasmModuleMachineFuncGroup_0.func_918(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 919:
          return Wat2WasmModuleMachineFuncGroup_0.func_919(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 920:
          return Wat2WasmModuleMachineFuncGroup_0.func_920(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 922:
          return Wat2WasmModuleMachineFuncGroup_0.func_922(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 950:
          return Wat2WasmModuleMachineFuncGroup_0.func_950(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 979:
          return Wat2WasmModuleMachineFuncGroup_0.func_979(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 994:
          return Wat2WasmModuleMachineFuncGroup_0.func_994(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
        case 995:
          return Wat2WasmModuleMachineFuncGroup_0.func_995(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramLong1, paramLong2 }, 14, i, instance)[0];
  }
  
  public static int call_indirect_15(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, int paramInt4, int paramInt5, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt5);
    int i = tableInstance.requiredRef(paramInt4);
    Instance instance = tableInstance.instance(paramInt4);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 732:
          return Wat2WasmModuleMachineFuncGroup_0.func_732(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramLong3, paramMemory, paramInstance);
        case 733:
          return Wat2WasmModuleMachineFuncGroup_0.func_733(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramLong3, paramMemory, paramInstance);
        case 992:
          return Wat2WasmModuleMachineFuncGroup_0.func_992(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramLong3, paramMemory, paramInstance);
        case 993:
          return Wat2WasmModuleMachineFuncGroup_0.func_993(paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramLong3, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramLong3 }, 15, i, instance)[0];
  }
  
  public static void call_indirect_16(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt6);
    int i = tableInstance.requiredRef(paramInt5);
    Instance instance = tableInstance.instance(paramInt5);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 54:
          Wat2WasmModuleMachineFuncGroup_0.func_54(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 63:
          Wat2WasmModuleMachineFuncGroup_0.func_63(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 73:
          Wat2WasmModuleMachineFuncGroup_0.func_73(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 114:
          Wat2WasmModuleMachineFuncGroup_0.func_114(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 137:
          Wat2WasmModuleMachineFuncGroup_0.func_137(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 146:
          Wat2WasmModuleMachineFuncGroup_0.func_146(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 201:
          Wat2WasmModuleMachineFuncGroup_0.func_201(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 206:
          Wat2WasmModuleMachineFuncGroup_0.func_206(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 207:
          Wat2WasmModuleMachineFuncGroup_0.func_207(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 225:
          Wat2WasmModuleMachineFuncGroup_0.func_225(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 451:
          Wat2WasmModuleMachineFuncGroup_0.func_451(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 452:
          Wat2WasmModuleMachineFuncGroup_0.func_452(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 459:
          Wat2WasmModuleMachineFuncGroup_0.func_459(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 564:
          Wat2WasmModuleMachineFuncGroup_0.func_564(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 566:
          Wat2WasmModuleMachineFuncGroup_0.func_566(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 569:
          Wat2WasmModuleMachineFuncGroup_0.func_569(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1086:
          Wat2WasmModuleMachineFuncGroup_0.func_1086(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1100:
          Wat2WasmModuleMachineFuncGroup_0.func_1100(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1151:
          Wat2WasmModuleMachineFuncGroup_0.func_1151(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1193:
          Wat2WasmModuleMachineFuncGroup_0.func_1193(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1208:
          Wat2WasmModuleMachineFuncGroup_0.func_1208(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1309:
          Wat2WasmModuleMachineFuncGroup_0.func_1309(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1666:
          Wat2WasmModuleMachineFuncGroup_0.func_1666(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1670:
          Wat2WasmModuleMachineFuncGroup_0.func_1670(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1686:
          Wat2WasmModuleMachineFuncGroup_0.func_1686(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1687:
          Wat2WasmModuleMachineFuncGroup_0.func_1687(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1688:
          Wat2WasmModuleMachineFuncGroup_0.func_1688(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1696:
          Wat2WasmModuleMachineFuncGroup_0.func_1696(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1718:
          Wat2WasmModuleMachineFuncGroup_0.func_1718(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1722:
          Wat2WasmModuleMachineFuncGroup_0.func_1722(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1758:
          Wat2WasmModuleMachineFuncGroup_0.func_1758(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1759:
          Wat2WasmModuleMachineFuncGroup_0.func_1759(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1760:
          Wat2WasmModuleMachineFuncGroup_0.func_1760(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1761:
          Wat2WasmModuleMachineFuncGroup_0.func_1761(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1763:
          Wat2WasmModuleMachineFuncGroup_0.func_1763(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
        case 1765:
          Wat2WasmModuleMachineFuncGroup_0.func_1765(paramInt1, paramInt2, paramInt3, paramInt4, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[4])[0] = paramInt1;
    (new long[4])[1] = paramInt2;
    (new long[4])[2] = paramInt3;
    (new long[4])[3] = paramInt4;
  }
  
  public static void call_indirect_17(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt8);
    int i = tableInstance.requiredRef(paramInt7);
    Instance instance = tableInstance.instance(paramInt7);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 210:
          Wat2WasmModuleMachineFuncGroup_0.func_210(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
          return;
        case 221:
          Wat2WasmModuleMachineFuncGroup_0.func_221(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
          return;
        case 1767:
          Wat2WasmModuleMachineFuncGroup_0.func_1767(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
          return;
        case 1771:
          Wat2WasmModuleMachineFuncGroup_0.func_1771(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
          return;
        case 1772:
          Wat2WasmModuleMachineFuncGroup_0.func_1772(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
          return;
        case 1773:
          Wat2WasmModuleMachineFuncGroup_0.func_1773(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[6])[0] = paramInt1;
    (new long[6])[1] = paramInt2;
    (new long[6])[2] = paramInt3;
    (new long[6])[3] = paramInt4;
    (new long[6])[4] = paramInt5;
    (new long[6])[5] = paramInt6;
  }
  
  public static void call_indirect_18(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt7);
    int i = tableInstance.requiredRef(paramInt6);
    Instance instance = tableInstance.instance(paramInt6);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 118:
          Wat2WasmModuleMachineFuncGroup_0.func_118(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 208:
          Wat2WasmModuleMachineFuncGroup_0.func_208(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 224:
          Wat2WasmModuleMachineFuncGroup_0.func_224(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 249:
          Wat2WasmModuleMachineFuncGroup_0.func_249(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 253:
          Wat2WasmModuleMachineFuncGroup_0.func_253(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 1310:
          Wat2WasmModuleMachineFuncGroup_0.func_1310(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 1691:
          Wat2WasmModuleMachineFuncGroup_0.func_1691(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 1764:
          Wat2WasmModuleMachineFuncGroup_0.func_1764(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 1766:
          Wat2WasmModuleMachineFuncGroup_0.func_1766(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 1768:
          Wat2WasmModuleMachineFuncGroup_0.func_1768(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 1769:
          Wat2WasmModuleMachineFuncGroup_0.func_1769(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 1770:
          Wat2WasmModuleMachineFuncGroup_0.func_1770(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
        case 1873:
          Wat2WasmModuleMachineFuncGroup_0.func_1873(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[5])[0] = paramInt1;
    (new long[5])[1] = paramInt2;
    (new long[5])[2] = paramInt3;
    (new long[5])[3] = paramInt4;
    (new long[5])[4] = paramInt5;
  }
  
  public static void call_indirect_19(int paramInt1, int paramInt2, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt2);
    int i = tableInstance.requiredRef(paramInt1);
    Instance instance = tableInstance.instance(paramInt1);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 15:
          Wat2WasmModuleMachineFuncGroup_0.func_15(paramMemory, paramInstance);
          return;
        case 16:
          Wat2WasmModuleMachineFuncGroup_0.func_16(paramMemory, paramInstance);
          return;
        case 18:
          Wat2WasmModuleMachineFuncGroup_0.func_18(paramMemory, paramInstance);
          return;
        case 35:
          Wat2WasmModuleMachineFuncGroup_0.func_35(paramMemory, paramInstance);
          return;
        case 45:
          Wat2WasmModuleMachineFuncGroup_0.func_45(paramMemory, paramInstance);
          return;
        case 46:
          Wat2WasmModuleMachineFuncGroup_0.func_46(paramMemory, paramInstance);
          return;
        case 74:
          Wat2WasmModuleMachineFuncGroup_0.func_74(paramMemory, paramInstance);
          return;
        case 75:
          Wat2WasmModuleMachineFuncGroup_0.func_75(paramMemory, paramInstance);
          return;
        case 76:
          Wat2WasmModuleMachineFuncGroup_0.func_76(paramMemory, paramInstance);
          return;
        case 85:
          Wat2WasmModuleMachineFuncGroup_0.func_85(paramMemory, paramInstance);
          return;
        case 215:
          Wat2WasmModuleMachineFuncGroup_0.func_215(paramMemory, paramInstance);
          return;
        case 1152:
          Wat2WasmModuleMachineFuncGroup_0.func_1152(paramMemory, paramInstance);
          return;
        case 1807:
          Wat2WasmModuleMachineFuncGroup_0.func_1807(paramMemory, paramInstance);
          return;
        case 1817:
          Wat2WasmModuleMachineFuncGroup_0.func_1817(paramMemory, paramInstance);
          return;
        case 1820:
          Wat2WasmModuleMachineFuncGroup_0.func_1820(paramMemory, paramInstance);
          return;
        case 1821:
          Wat2WasmModuleMachineFuncGroup_0.func_1821(paramMemory, paramInstance);
          return;
        case 1824:
          Wat2WasmModuleMachineFuncGroup_0.func_1824(paramMemory, paramInstance);
          return;
        case 1826:
          Wat2WasmModuleMachineFuncGroup_0.func_1826(paramMemory, paramInstance);
          return;
        case 1849:
          Wat2WasmModuleMachineFuncGroup_0.func_1849(paramMemory, paramInstance);
          return;
        case 1858:
          Wat2WasmModuleMachineFuncGroup_0.func_1858(paramMemory, paramInstance);
          return;
        case 1874:
          Wat2WasmModuleMachineFuncGroup_0.func_1874(paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
  }
  
  public static long call_indirect_20(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt4);
    int i = tableInstance.requiredRef(paramInt3);
    Instance instance = tableInstance.instance(paramInt3);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1837:
          return Wat2WasmModuleMachineFuncGroup_0.func_1837(paramInt1, paramLong, paramInt2, paramMemory, paramInstance);
        case 1838:
          return Wat2WasmModuleMachineFuncGroup_0.func_1838(paramInt1, paramLong, paramInt2, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramLong, paramInt2 }, 20, i, instance)[0];
  }
  
  public static int call_indirect_21(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt5);
    int i = tableInstance.requiredRef(paramInt4);
    Instance instance = tableInstance.instance(paramInt4);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 10:
          return Wat2WasmModuleMachineFuncGroup_0.func_10(paramInt1, paramLong, paramInt2, paramInt3, paramMemory, paramInstance);
        case 1802:
          return Wat2WasmModuleMachineFuncGroup_0.func_1802(paramInt1, paramLong, paramInt2, paramInt3, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramLong, paramInt2, paramInt3 }, 21, i, instance)[0];
  }
  
  public static int call_indirect_22(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2, int paramInt6, int paramInt7, int paramInt8, int paramInt9, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt9);
    int i = tableInstance.requiredRef(paramInt8);
    Instance instance = tableInstance.instance(paramInt8);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 13:
          return Wat2WasmModuleMachineFuncGroup_0.func_13(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramLong1, paramLong2, paramInt6, paramInt7, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramLong1, paramLong2, paramInt6, paramInt7 }, 22, i, instance)[0];
  }
  
  public static void call_indirect_23(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt10);
    int i = tableInstance.requiredRef(paramInt9);
    Instance instance = tableInstance.instance(paramInt9);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 87:
          Wat2WasmModuleMachineFuncGroup_0.func_87(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramMemory, paramInstance);
          return;
        case 1598:
          Wat2WasmModuleMachineFuncGroup_0.func_1598(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[8])[0] = paramInt1;
    (new long[8])[1] = paramInt2;
    (new long[8])[2] = paramInt3;
    (new long[8])[3] = paramInt4;
    (new long[8])[4] = paramInt5;
    (new long[8])[5] = paramInt6;
    (new long[8])[6] = paramInt7;
    (new long[8])[7] = paramInt8;
  }
  
  public static void call_indirect_24(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt9);
    int i = tableInstance.requiredRef(paramInt8);
    Instance instance = tableInstance.instance(paramInt8);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 88:
          Wat2WasmModuleMachineFuncGroup_0.func_88(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
          return;
        case 222:
          Wat2WasmModuleMachineFuncGroup_0.func_222(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
          return;
        case 1643:
          Wat2WasmModuleMachineFuncGroup_0.func_1643(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[7])[0] = paramInt1;
    (new long[7])[1] = paramInt2;
    (new long[7])[2] = paramInt3;
    (new long[7])[3] = paramInt4;
    (new long[7])[4] = paramInt5;
    (new long[7])[5] = paramInt6;
    (new long[7])[6] = paramInt7;
  }
  
  public static void call_indirect_25(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt4);
    int i = tableInstance.requiredRef(paramInt3);
    Instance instance = tableInstance.instance(paramInt3);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 174:
          Wat2WasmModuleMachineFuncGroup_0.func_174(paramInt1, paramLong, paramInt2, paramMemory, paramInstance);
          return;
        case 175:
          Wat2WasmModuleMachineFuncGroup_0.func_175(paramInt1, paramLong, paramInt2, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[3])[0] = paramInt1;
    (new long[3])[1] = paramLong;
    (new long[3])[2] = paramInt2;
  }
  
  public static long call_indirect_26(int paramInt1, long paramLong, int paramInt2, int paramInt3, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt3);
    int i = tableInstance.requiredRef(paramInt2);
    Instance instance = tableInstance.instance(paramInt2);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 193:
          return Wat2WasmModuleMachineFuncGroup_0.func_193(paramInt1, paramLong, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramLong }, 26, i, instance)[0];
  }
  
  public static int call_indirect_27(int paramInt1, int paramInt2, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt2);
    int i = tableInstance.requiredRef(paramInt1);
    Instance instance = tableInstance.instance(paramInt1);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 240:
          return Wat2WasmModuleMachineFuncGroup_0.func_240(paramMemory, paramInstance);
        case 241:
          return Wat2WasmModuleMachineFuncGroup_0.func_241(paramMemory, paramInstance);
        case 1631:
          return Wat2WasmModuleMachineFuncGroup_0.func_1631(paramMemory, paramInstance);
        case 1677:
          return Wat2WasmModuleMachineFuncGroup_0.func_1677(paramMemory, paramInstance);
        case 1775:
          return Wat2WasmModuleMachineFuncGroup_0.func_1775(paramMemory, paramInstance);
        case 1791:
          return Wat2WasmModuleMachineFuncGroup_0.func_1791(paramMemory, paramInstance);
        case 1857:
          return Wat2WasmModuleMachineFuncGroup_0.func_1857(paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[0], 27, i, instance)[0];
  }
  
  public static int call_indirect_28(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2, int paramInt5, int paramInt6, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt6);
    int i = tableInstance.requiredRef(paramInt5);
    Instance instance = tableInstance.instance(paramInt5);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 374:
          return Wat2WasmModuleMachineFuncGroup_0.func_374(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 375:
          return Wat2WasmModuleMachineFuncGroup_0.func_375(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 376:
          return Wat2WasmModuleMachineFuncGroup_0.func_376(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 377:
          return Wat2WasmModuleMachineFuncGroup_0.func_377(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 378:
          return Wat2WasmModuleMachineFuncGroup_0.func_378(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 379:
          return Wat2WasmModuleMachineFuncGroup_0.func_379(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 403:
          return Wat2WasmModuleMachineFuncGroup_0.func_403(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 404:
          return Wat2WasmModuleMachineFuncGroup_0.func_404(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 405:
          return Wat2WasmModuleMachineFuncGroup_0.func_405(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
        case 429:
          return Wat2WasmModuleMachineFuncGroup_0.func_429(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2 }, 28, i, instance)[0];
  }
  
  public static int call_indirect_29(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2, long paramLong3, int paramInt5, int paramInt6, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt6);
    int i = tableInstance.requiredRef(paramInt5);
    Instance instance = tableInstance.instance(paramInt5);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 426:
          return Wat2WasmModuleMachineFuncGroup_0.func_426(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramLong3, paramMemory, paramInstance);
        case 427:
          return Wat2WasmModuleMachineFuncGroup_0.func_427(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramLong3, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramLong3 }, 29, i, instance)[0];
  }
  
  public static int call_indirect_30(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt10);
    int i = tableInstance.requiredRef(paramInt9);
    Instance instance = tableInstance.instance(paramInt9);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 827:
          return Wat2WasmModuleMachineFuncGroup_0.func_827(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8 }, 30, i, instance)[0];
  }
  
  public static int call_indirect_31(int paramInt1, long paramLong1, int paramInt2, long paramLong2, int paramInt3, int paramInt4, int paramInt5, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt5);
    int i = tableInstance.requiredRef(paramInt4);
    Instance instance = tableInstance.instance(paramInt4);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 829:
          return Wat2WasmModuleMachineFuncGroup_0.func_829(paramInt1, paramLong1, paramInt2, paramLong2, paramInt3, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramLong1, paramInt2, paramLong2, paramInt3 }, 31, i, instance)[0];
  }
  
  public static int call_indirect_32(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt8);
    int i = tableInstance.requiredRef(paramInt7);
    Instance instance = tableInstance.instance(paramInt7);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1805:
          return Wat2WasmModuleMachineFuncGroup_0.func_1805(paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramInt5, paramInt6, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return (int)Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramLong1, paramLong2, paramInt5, paramInt6 }, 32, i, instance)[0];
  }
  
  public static double call_indirect_33(double paramDouble, int paramInt1, int paramInt2, int paramInt3, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt3);
    int i = tableInstance.requiredRef(paramInt2);
    Instance instance = tableInstance.instance(paramInt2);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1869:
          return Wat2WasmModuleMachineFuncGroup_0.func_1869(paramDouble, paramInt1, paramMemory, paramInstance);
        case 1881:
          return Wat2WasmModuleMachineFuncGroup_0.func_1881(paramDouble, paramInt1, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { Value.doubleToLong(paramDouble), paramInt1 }, 33, i, instance)[0]);
  }
  
  public static void call_indirect_34(int paramInt1, long paramLong, int paramInt2, int paramInt3, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt3);
    int i = tableInstance.requiredRef(paramInt2);
    Instance instance = tableInstance.instance(paramInt2);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1879:
          Wat2WasmModuleMachineFuncGroup_0.func_1879(paramInt1, paramLong, paramMemory, paramInstance);
          return;
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    (new long[2])[0] = paramInt1;
    (new long[2])[1] = paramLong;
  }
  
  public static double call_indirect_35(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt2);
    int i = tableInstance.requiredRef(paramInt1);
    Instance instance = tableInstance.instance(paramInt1);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1882:
          return Wat2WasmModuleMachineFuncGroup_0.func_1882(paramDouble1, paramDouble2, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { Value.doubleToLong(paramDouble1), Value.doubleToLong(paramDouble2) }, 35, i, instance)[0]);
  }
  
  public static double call_indirect_36(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt5);
    int i = tableInstance.requiredRef(paramInt4);
    Instance instance = tableInstance.instance(paramInt4);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1883:
          return Wat2WasmModuleMachineFuncGroup_0.func_1883(paramInt1, paramInt2, paramInt3, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3 }, 36, i, instance)[0]);
  }
  
  public static double call_indirect_37(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt7);
    int i = tableInstance.requiredRef(paramInt6);
    Instance instance = tableInstance.instance(paramInt6);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1884:
          return Wat2WasmModuleMachineFuncGroup_0.func_1884(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2, paramInt3, paramInt4, paramInt5 }, 37, i, instance)[0]);
  }
  
  public static long call_indirect_38(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt4);
    int i = tableInstance.requiredRef(paramInt3);
    Instance instance = tableInstance.instance(paramInt3);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1885:
          return Wat2WasmModuleMachineFuncGroup_0.func_1885(paramInt1, paramInt2, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2 }, 38, i, instance)[0];
  }
  
  public static float call_indirect_39(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt4);
    int i = tableInstance.requiredRef(paramInt3);
    Instance instance = tableInstance.instance(paramInt3);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1886:
          return Wat2WasmModuleMachineFuncGroup_0.func_1886(paramInt1, paramInt2, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Value.longToFloat(Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2 }, 39, i, instance)[0]);
  }
  
  public static double call_indirect_40(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Memory paramMemory, Instance paramInstance) {
    Wat2WasmModuleMachineShaded.checkInterruption();
    TableInstance tableInstance = paramInstance.table(paramInt4);
    int i = tableInstance.requiredRef(paramInt3);
    Instance instance = tableInstance.instance(paramInt3);
    if (instance == null || instance == paramInstance) {
      switch (i) {
        case 1887:
          return Wat2WasmModuleMachineFuncGroup_0.func_1887(paramInt1, paramInt2, paramMemory, paramInstance);
      } 
      throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
    } 
    return Value.longToDouble(Wat2WasmModuleMachineShaded.callIndirect(new long[] { paramInt1, paramInt2 }, 40, i, instance)[0]);
  }
}


/* Location:              /home/andreatp/workspace/chicory6/wabt/target/original-wabt-999-SNAPSHOT.jar!/com/dylibso/chicory/wabt/Wat2WasmModuleMachine.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */