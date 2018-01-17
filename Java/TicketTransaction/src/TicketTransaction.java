public class TicketTransaction {
	private int num = 10; // 设置当前总票数
	private Object synObj = new Object();
	private boolean isRun = true;

	public TicketTransaction() {
		// 创建具备卖票功能的类对象
		Sell sellRunnable = new Sell();
		Refund refundRunnable = new Refund();
		// 创建4个卖票线程
		Thread sellA = new Thread(sellRunnable, "SellA");
		Thread sellB = new Thread(sellRunnable, "SellB");
		Thread sellC = new Thread(sellRunnable, "SellC");
		Thread sellD = new Thread(sellRunnable, "SellD");
		// 分别启动线程
		sellA.start();
		sellB.start();
		sellC.start();
		sellD.start();
		
		// 创建4个退票线程
		Thread refundA = new Thread(refundRunnable, "RefundA");
		Thread refundB = new Thread(refundRunnable, "RefundB");
		//只有两个退票程序，只要不加入refundC和refundD就可以了
		Thread refundC = new Thread(refundRunnable, "RefundC");
		Thread refundD = new Thread(refundRunnable, "RefundD");
		// 分别启动线程
		refundA.start();
		refundB.start();
		refundC.start();
		refundD.start();
	}

	/**
	 * 表示卖票线程类(内部类)
	 * @author Administrator
	 */
	private class Sell implements Runnable {
		@Override
		public void run() {
			while (isRun) {
				synchronized (synObj) {
					// 如果有票可卖
					if (num > 0) {
						// 卖出一张票
						System.out.println(Thread.currentThread().getName()
								+ "-tickets leaving: " + --num);
					}
					else{ // 如果没票卖，结束卖票任务
						//isRun = false;
						System.out.println(Thread.currentThread().getName()
								+ "---Wait");
						try {
							synObj.notify();
							synObj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				// 卖出一张票后休息0.1秒
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 表示退票线程类(内部类)
	 */
	private class Refund implements Runnable {
		@Override
		public void run() {
			while (isRun) {
				synchronized (synObj) {
					// 如果有票可退
					if (num < 10) {
						// 退回一张票
						System.out.println(Thread.currentThread().getName()
								+ "-tickets leaving: " + ++num);
					}
					else{ // 如果没票退，结束退票任务
						//isRun = false;
						System.out.println(Thread.currentThread().getName()
								+ "---Wait");
						try {
							synObj.notify();
							synObj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				// 退回一张票后休息0.1秒
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public static void main(String[] args) {
		TicketTransaction t = new TicketTransaction(); // 实例化类对象
		// 5秒后关闭应用程序
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 结束应用程序
		System.out.println("Done");
		System.exit(0);

	}

}