package com.homw.modbus;

import org.junit.Test;

import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.rtu.ModbusRTUFrameFactory;

/**
 * Modbus客户端测试
 * @author Hom
 * @version 1.0
 * @since 2018年11月7日
 *
 */
public class ModbusClientTest {
	private static final int process_factor = 500;

	@Test
	public void test() throws Exception {
		ModbusClient client = ModbusClientFactory.create();

		ModbusFrame openAllFrame = ModbusRTUFrameFactory.writeSingleCoil((short) 1, 0, true);
		ModbusFrame closeAllFrame = ModbusRTUFrameFactory.writeSingleCoil((short) 1, 0, false);

		ModbusFrame openLoop1Frame = ModbusRTUFrameFactory.writeSingleCoil((short) 1, 3, true);
		ModbusFrame closeLoop1Frame = ModbusRTUFrameFactory.writeSingleCoil((short) 1, 3, false);

		client.send(openLoop1Frame);

		Thread.sleep(3000);

		client.send(closeLoop1Frame);
		
		//while(true){}
		
		/*while (true) 
		{
			// 读线圈
			try {
				client.send(ModbusRTUFrameFactory.readCoil((short) 0x11, 0x13, 0x25));
			} catch (ModbusTimeoutException e) {
				//e.printStackTrace();
			}
			
			Thread.sleep((long) (Math.random() * process_factor));
			
			// 读离散输入
			//client.send(ModbusRTUFrameFactory.readDiscreteInput((short) 0x11, 0x13, 0x25));
			
			//Thread.sleep((long) (Math.random() * process_factor));
			
			// 写单个线圈
			try {
				client.send(ModbusRTUFrameFactory.writeSingleCoil((short) 0x11, 0xAC, true));
			} catch (ModbusTimeoutException e) {
				//e.printStackTrace();
			}
			
			Thread.sleep((long) (Math.random() * process_factor));
			
			// 写多个线圈
			try {
				client.send(ModbusRTUFrameFactory.writeMultiCoil((short) 0x11, 0x13, 0x0A,
						BitSet.valueOf(new byte[] { (byte) 0xCD, 0x01 })));
			} catch (ModbusTimeoutException e) {
				//e.printStackTrace();
			}
		}*/
	}
}
