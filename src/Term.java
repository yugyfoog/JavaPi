import java.util.concurrent.CountDownLatch;

public class Term implements Runnable {
	private char[] T;
	private int a, b;
	private final CountDownLatch done_signal;

	public Term(char[] _T, int _a, int _b, CountDownLatch _done_signal) {
		T = _T;
		a = _a;
		b = _b;
		done_signal = _done_signal;
	}
	
	@Override
	public void run() {
		Pi.arccot(T, b);
		Pi.multiply(T, a);
		done_signal.countDown();
	}

}
