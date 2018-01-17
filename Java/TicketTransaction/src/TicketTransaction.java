public class TicketTransaction {
	private int num = 10; // ���õ�ǰ��Ʊ��
	private Object synObj = new Object();
	private boolean isRun = true;

	public TicketTransaction() {
		// �����߱���Ʊ���ܵ������
		Sell sellRunnable = new Sell();
		Refund refundRunnable = new Refund();
		// ����4����Ʊ�߳�
		Thread sellA = new Thread(sellRunnable, "SellA");
		Thread sellB = new Thread(sellRunnable, "SellB");
		Thread sellC = new Thread(sellRunnable, "SellC");
		Thread sellD = new Thread(sellRunnable, "SellD");
		// �ֱ������߳�
		sellA.start();
		sellB.start();
		sellC.start();
		sellD.start();
		
		// ����4����Ʊ�߳�
		Thread refundA = new Thread(refundRunnable, "RefundA");
		Thread refundB = new Thread(refundRunnable, "RefundB");
		//ֻ��������Ʊ����ֻҪ������refundC��refundD�Ϳ�����
		Thread refundC = new Thread(refundRunnable, "RefundC");
		Thread refundD = new Thread(refundRunnable, "RefundD");
		// �ֱ������߳�
		refundA.start();
		refundB.start();
		refundC.start();
		refundD.start();
	}

	/**
	 * ��ʾ��Ʊ�߳���(�ڲ���)
	 * @author Administrator
	 */
	private class Sell implements Runnable {
		@Override
		public void run() {
			while (isRun) {
				synchronized (synObj) {
					// �����Ʊ����
					if (num > 0) {
						// ����һ��Ʊ
						System.out.println(Thread.currentThread().getName()
								+ "-tickets leaving: " + --num);
					}
					else{ // ���ûƱ����������Ʊ����
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
				// ����һ��Ʊ����Ϣ0.1��
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ��ʾ��Ʊ�߳���(�ڲ���)
	 */
	private class Refund implements Runnable {
		@Override
		public void run() {
			while (isRun) {
				synchronized (synObj) {
					// �����Ʊ����
					if (num < 10) {
						// �˻�һ��Ʊ
						System.out.println(Thread.currentThread().getName()
								+ "-tickets leaving: " + ++num);
					}
					else{ // ���ûƱ�ˣ�������Ʊ����
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
				// �˻�һ��Ʊ����Ϣ0.1��
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public static void main(String[] args) {
		TicketTransaction t = new TicketTransaction(); // ʵ���������
		// 5���ر�Ӧ�ó���
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// ����Ӧ�ó���
		System.out.println("Done");
		System.exit(0);

	}

}