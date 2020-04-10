package com.homw.tool.api.kede.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.common.util.DateUtil;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//@Service
public class CommPortService implements ICommPortService {

	private static Logger logger = LoggerFactory.getLogger(CommPortService.class);
	
	private static final int BAUD = 2400;
	private static ReentrantLock lock = new ReentrantLock();

	private static SerialPort serialPort;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	@SuppressWarnings("rawtypes")
	public void init() {
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				try {
					serialPort = (SerialPort) portId.open("DEVICE", 3000);

					outputStream = serialPort.getOutputStream();
					inputStream = serialPort.getInputStream();

					serialPort.notifyOnDataAvailable(true);
					serialPort.addEventListener(new SerialEventListener());
					serialPort.setSerialPortParams(BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_EVEN);
				} catch (Exception e) {
					logger.error("error:", e);
				} 
			}
		}
	}

	@Override
	public void sendOpenElecMsg(String elecAddr) throws Exception {
		if (lock.tryLock(3000, TimeUnit.MILLISECONDS)) {
			try {
				String commd = "68" + DeviceUtils.lowToTop(elecAddr) + "681C10CB333333343333334E3387873C5C3449"; // 开电源固定的一个字符串指令
				String msg = commd + DeviceUtils.checkData(commd) + 16;
				logger.info("send data: " + msg);
				byte[] bymsg = DeviceUtils.hexStringToBytes(msg);

				outputStream.write(bymsg);
				outputStream.flush();

				logger.info("open success, update restored status.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (lock != null && lock.isLocked()) {
					Thread.sleep(500);
					lock.unlock();
				}
			}
		} else {
			throw new Exception("sendOpenElecMsg is not lockFail");
		}
	}

	@Override
	public void sendCloseElecMsg(String elecAddr) throws Exception {
		if (lock.tryLock(3000, TimeUnit.MILLISECONDS)) {
			try {
				String commd = "68" + DeviceUtils.lowToTop(elecAddr) + "681C10CB333333343333334D3389873C5C3449";
				String msg = commd + DeviceUtils.checkData(commd) + 16;
				logger.info("send data: " + msg);
				byte[] bymsg = DeviceUtils.hexStringToBytes(msg);

				outputStream.write(bymsg); // 函数是没有返回值的。
				outputStream.flush();

				logger.info("close success, update restored status.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (lock != null && lock.isLocked()) {
					Thread.sleep(500);
					lock.unlock();
				}
			}
		} else {
			throw new Exception("sendCloseElecMsg is not lockFail");
		}
	}

	@Override
	public void sendSearchElecMsg(String elecAddr) throws Exception {
		if (lock.tryLock(3000, TimeUnit.MILLISECONDS)) {
			try {
				String commd = "68" + DeviceUtils.lowToTop(elecAddr) + "6803083235B43A34333333";// 最后的一串数字代码操作的动作(抄表、开、关)
				String msg = commd + DeviceUtils.checkData(commd) + 16;// 校验和
				logger.info("send data: " + msg);
				byte[] bymsg = DeviceUtils.hexStringToBytes(msg);

				outputStream.write(bymsg);
				outputStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (lock != null && lock.isLocked()) {
					Thread.sleep(500);
					lock.unlock();
				}
			}
		} else {
			throw new Exception("sendSearchElecMsg is not lockFail");
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
					byte[] readBuf = new byte[1024]; // 声明一个存储接收数据的缓冲区 用来接收缓冲区收到的数据
					byte[] totalBuf = new byte[] {};
					while ((len = inputStream.read(readBuf)) > 0) {
						totalBuf = DeviceUtils.byteMerge(totalBuf, readBuf, len);
						readBuf = new byte[1024];
					}
					
					String data = DeviceUtils.bytesToHexString(totalBuf);
					logger.info("data:{}", data);
					if (data.length() > 24) {
						// 数据长度大于24为抄表的数据
						String status = data.substring(20, 28);// 返回的是开和关的状态
						String statusparam = "3235b43a";// 用来判断此结果是否是抄表返回的数据
						if (status.equals(statusparam)) {
							String addr = data.substring(2, 14);// 返回数据里取的电表地址

							logger.info("addr:{}", addr);
							addr = DeviceUtils.lowToTop(addr);// 所有返回的地址都是反序的，这里进行正序操作
							logger.info("addr:{}", addr);

							String elecStatus = DeviceUtils.sub33(data.substring(62, 66));
							logger.info("elecStatus:{}", elecStatus);

							Integer elecUsePoint = Integer
									.parseInt(DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(38, 46))));
							logger.info("elecUsePoint:{}", elecUsePoint);

							logger.info("解析：当前剩余正负号: " + DeviceUtils.sub33(data.substring(28, 30))); // [29,30]
							logger.info("解析：剩余电量: " + DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(30, 38))));
							logger.info("解析：累计电量: " + DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(38, 46))));
							logger.info("解析：够电次数: " + DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(46, 50))));
							logger.info("解析：用户户号: " + DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(50, 54))));
							logger.info("解析：A相: " + DeviceUtils.sub33(data.substring(54, 56)));
							logger.info("解析：当前功率: " + DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(56, 62))));
							logger.info("x解析：继电器状态: " + DeviceUtils.sub33(data.substring(62, 66)) + " , "
									+ DeviceUtils.chooseState(DeviceUtils.sub33(data.substring(62, 66))));
							logger.info("解析：当前电压: " + DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(66, 70))));
							logger.info("解析：当前电流: " + DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(70, 74))));
							logger.info("解析：功率因素: " + DeviceUtils.lowToTop(DeviceUtils.sub33(data.substring(74, 78))));
						}
					} else {
						// 不大于24就是开 关的返回数据
						String addr = data.substring(2, 14);
						addr = DeviceUtils.lowToTop(addr);
						try {
							sendSearchElecMsg(addr);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("error addr:{}", addr);
						}
						logger.info("success");
					}
					logger.info("recv data:{}", data);
				} catch (IOException e) {
					e.printStackTrace();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
				break;
			}
		}
	}

	@PostConstruct
	public void checkComm() {
		init();
		/*new Thread(new CheckThread()).start();
		new Thread(new TimeThread()).start();*/
	}

	class CheckThread implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					if (inputStream == null && outputStream == null) {
						init();
					}
					Thread.sleep(5000);
				}
			} catch (Exception e) {
			}
		}
	}

	class TimeThread implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					logger.info("run..........." + DateUtil.formatDateTime(System.currentTimeMillis()));
					close();
					Thread.sleep(3600000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void close() throws Exception {
		if (inputStream != null) {
			inputStream.close();
			inputStream = null;
		}
		if (outputStream != null) {
			outputStream.close();
			outputStream = null;
		}
		if (serialPort != null) {
			serialPort.notifyOnDataAvailable(false);
			serialPort.close();
		}
	}

}