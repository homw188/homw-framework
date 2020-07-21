package com.homw.tool.api.kede.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import com.homw.tool.annotation.CommPortCondition;
import com.homw.tool.api.kede.KedeProtocolUtil;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @description 科德电表串口服务
 * @author Hom
 * @version 1.0
 * @since 2020-07-21
 */
@Service
@Conditional(CommPortCondition.class)
public class CommPortService implements ICommPortService {

	private static Logger logger = LoggerFactory.getLogger(CommPortService.class);

	private SerialPort commPort;
	private InputStream inStream;
	private OutputStream outStream;
	private ReentrantLock lock = new ReentrantLock();
	
	private static final int BAUD = 2400;// 波特率

	@PostConstruct
	public void init() {
		scanCommPort();

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (inStream == null && outStream == null) {
					scanCommPort();
				}
			}
		}, 5000, 5000);

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					closeCommPort();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 3600000, 3600000);
	}

	@Override
	public void openElec(String elecAddr) throws Exception {
		if (lock.tryLock(3000, TimeUnit.MILLISECONDS)) {
			try {
				String commd = "68" + KedeProtocolUtil.revertEndian(elecAddr)
						+ "681C10CB333333343333334E3387873C5C3449"; // 开电源指令
				String msg = commd + KedeProtocolUtil.checknum(commd) + "16";
				logger.info("send data: " + msg);
				
				outStream.write(KedeProtocolUtil.hexStrToBytes(msg));
				outStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Thread.sleep(500);
				lock.unlock();
			}
		} else {
			throw new Exception("get lock failed");
		}
	}

	@Override
	public void closeElec(String elecAddr) throws Exception {
		if (lock.tryLock(3000, TimeUnit.MILLISECONDS)) {
			try {
				String commd = "68" + KedeProtocolUtil.revertEndian(elecAddr)
						+ "681C10CB333333343333334D3389873C5C3449";
				String msg = commd + KedeProtocolUtil.checknum(commd) + 16;
				logger.info("send data: " + msg);

				outStream.write(KedeProtocolUtil.hexStrToBytes(msg));
				outStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Thread.sleep(500);
				lock.unlock();
			}
		} else {
			throw new Exception("get lock failed");
		}
	}

	@Override
	public void searchElec(String elecAddr) throws Exception {
		if (lock.tryLock(3000, TimeUnit.MILLISECONDS)) {
			try {
				String commd = "68" + KedeProtocolUtil.revertEndian(elecAddr) + "6803083235B43A34333333";
				String msg = commd + KedeProtocolUtil.checknum(commd) + 16;
				logger.info("send data: " + msg);

				outStream.write(KedeProtocolUtil.hexStrToBytes(msg));
				outStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Thread.sleep(500);
				lock.unlock();
			}
		} else {
			throw new Exception("get lock failed");
		}
	}

	@SuppressWarnings("rawtypes")
	public void scanCommPort() {
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				try {
					// open application
					commPort = (SerialPort) portId.open("DEVICE", 3000);

					outStream = commPort.getOutputStream();
					inStream = commPort.getInputStream();

					commPort.notifyOnDataAvailable(true);
					commPort.addEventListener(new SerialEventListener());
					commPort.setSerialPortParams(BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_EVEN);
				} catch (Exception e) {
					logger.error("open comm port error: ", e);
				}
			}
		}
	}

	public void closeCommPort() throws Exception {
		if (inStream != null) {
			inStream.close();
			inStream = null;
		}
		if (outStream != null) {
			outStream.close();
			outStream = null;
		}
		if (commPort != null) {
			commPort.notifyOnDataAvailable(false);
			commPort.close();
		}
	}

	class SerialEventListener implements SerialPortEventListener {
		@Override
		public void serialEvent(SerialPortEvent event) {
			logger.info("serialEventListener type:{}", event.getEventType());
			switch (event.getEventType()) {
			case SerialPortEvent.BI:
				break;/* Break interrupt,通讯中断 */
			case SerialPortEvent.OE:
				break;/* Overrun error，溢位错误 */
			case SerialPortEvent.FE:
				break;/* Framing error，传帧错误 */
			case SerialPortEvent.PE:
				break;/* Parity error，奇偶校验错误 */
			case SerialPortEvent.CD:
				break;/* Carrier detect，载波检测 */
			case SerialPortEvent.CTS:
				break;/* Clear to send，清除发送 */
			case SerialPortEvent.DSR:
				break;/* Data set ready，数据设备就绪 */
			case SerialPortEvent.RI:
				break; /* Ring indicator，响铃指示 振铃指示 */
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;/* Output buffer is empty，输出缓冲区清空 */
			case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据时读取数据,并且给串口返回数据
				try {
					if (lock != null && lock.isLocked()) {
						lock.unlock();
					}

					int len = -1;
					// 接收缓冲区
					byte[] readBuf = new byte[1024];
					byte[] totalBuf = new byte[] {};
					while ((len = inStream.read(readBuf)) > 0) {
						totalBuf = KedeProtocolUtil.mergeBytes(totalBuf, readBuf, len);
						readBuf = new byte[1024];
					}

					String data = KedeProtocolUtil.bytesToHexStr(totalBuf);
					logger.info("recv data:{}", data);
					if (data.length() > 24) {
						// 抄表的数据
						String status = data.substring(20, 28);// 返回的是开和关的状态
						String statusparam = "3235b43a";// 用来判断此结果是否是抄表返回的数据
						if (status.equals(statusparam)) {
							String addr = data.substring(2, 14);// 返回数据里取的电表地址
							addr = KedeProtocolUtil.revertEndian(addr);// 所有返回的地址都是反序的，这里进行正序操作
							logger.info("addr:{}", addr);

							String elecStatus = KedeProtocolUtil.sub33H(data.substring(62, 66));
							logger.info("elecStatus:{}", elecStatus);

							Integer elecUsePoint = Integer.parseInt(KedeProtocolUtil.sub33H(data.substring(38, 46)));
							logger.info("elecUsePoint:{}", elecUsePoint);

							logger.info("解析：当前剩余正负号: " + KedeProtocolUtil.sub33H(data.substring(28, 30))); // [29,30]
							logger.info("解析：剩余电量: " + KedeProtocolUtil.sub33H(data.substring(30, 38)));
							logger.info("解析：累计电量: " + KedeProtocolUtil.sub33H(data.substring(38, 46)));
							logger.info("解析：够电次数: " + KedeProtocolUtil.sub33H(data.substring(46, 50)));
							logger.info("解析：用户户号: " + KedeProtocolUtil.sub33H(data.substring(50, 54)));
							logger.info("解析：A相: " + KedeProtocolUtil.sub33H(data.substring(54, 56)));
							logger.info("解析：当前功率: " + KedeProtocolUtil.sub33H(data.substring(56, 62)));
							logger.info("x解析：继电器状态: " + KedeProtocolUtil.sub33H(data.substring(62, 66)) + " , "
									+ KedeProtocolUtil.tripState(KedeProtocolUtil.sub33H(data.substring(62, 66))));
							logger.info("解析：当前电压: " + KedeProtocolUtil.sub33H(data.substring(66, 70)));
							logger.info("解析：当前电流: " + KedeProtocolUtil.sub33H(data.substring(70, 74)));
							logger.info("解析：功率因素: " + KedeProtocolUtil.sub33H(data.substring(74, 78)));
						}
					} else {
						// 开关指令的返回数据
						String addr = data.substring(2, 14);
						addr = KedeProtocolUtil.revertEndian(addr);
						try {
							searchElec(addr);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("search error addr:{}", addr);
						}
						logger.info("success");
					}
				} catch (IOException e) {
					e.printStackTrace();
					try {
						closeCommPort();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}