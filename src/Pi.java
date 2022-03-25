import java.util.concurrent.CountDownLatch;

public class Pi {

	static final double LOG_BASE = 16.0*Math.log10(2.0);

	public static void main(String[] args) throws InterruptedException {
		int digits = 1000;
		if (args.length == 1)
			digits = Integer.parseInt(args[0]);
		
		int size = (int)(digits/LOG_BASE) + 3;
		
		char[] t1 = new char[size];
		char[] t2 = new char[size];
		char[] t3 = new char[size];
		char[] t4 = new char[size];

		CountDownLatch done_signal = new CountDownLatch(4);
		
		(new Thread(new Term(t1, 176, 57, done_signal))).start();
		(new Thread(new Term(t2, 28, 239, done_signal))).start();
		(new Thread(new Term(t3, 48, 682, done_signal))).start();
		(new Thread(new Term(t4, 96, 12943, done_signal))).start();
		done_signal.await();
		
		add(t1, t2);
		subtract(t1, t3);
		add(t1, t4);
		print(t1, digits);
		System.out.println();
	}

	private static void print(char U[], int d) {
		System.out.print((int)U[U.length-1]); // the 3
		U[U.length-1] = 0;
		System.out.print('.');;
		while (d > 3) {
			multiply(U, 10000);
			System.out.format("%04d", (int)U[U.length-1]);
			U[U.length-1] = 0;
			d -= 4;
		}
		switch (d) {
		case 1:
			multiply(U, 10);
			System.out.format("%01d", (int)U[U.length-1]);
			break;
		case 2:
			multiply(U, 100);
			System.out.format("%02d", (int)U[U.length-1]);
			break;
		case 3:
			multiply(U, 1000);
			System.out.format("%03d", (int)U[U.length-1]);
			break;
		}
	}
	
	public static void multiply(char[] U, int v) {
		long k = 0;
		for (int i = 0; i < U.length; i++) {
			k += (long)U[i]*(long)v;
			U[i] = (char)k;
			k >>= 16;
		}
	}

	private static void subtract(char[] U, char[] V) {
		long k = 0;
		for (int i = 0; i < U.length; i++) {
			k += (long)U[i] - (long)V[i];
			U[i] = (char)k;
			k >>= 16;
		}
	}

	private static void add(char[] U, char[] V) {
		long k = 0;
		for (int i = 0; i < U.length; i++) {
			k += (long)U[i] + (long)V[i];
			U[i] = (char)k;
			k >>= 16;
		}
	}

	public static void arccot(char[] S, int x) {
		char A[] = new char[S.length];
		char B[] = new char[S.length];
		int x2 = x*x;
		
		set(A, 1);
		divide(A, x);
		copy(S, A);
		for (int i = 3;; i += 2) {
			divide(A, x2);
			copy(B, A);
			divide(B, i);
			if (iszero(B))
				break;
			if ((i&2) != 0)
				subtract(S, B);
			else
				add(S, B);
		}
	}

	private static boolean iszero(char[] U) {
		for (int x : U)
			if (x != 0)
				return false;
		return true;
	}

	private static void copy(char[] U, char[] V) {
		for (int i = 0; i < U.length; i++)
			U[i] = V[i];
	}

	private static void divide(char[] U, int v) {
		long k = 0;
		for (int i = U.length-1; i >= 0; i--) {
			k = (k << 16) + U[i];
			U[i] = (char)(k/v);
			k %= v;
		}
	}

	private static void set(char[] U, int v) {
		for (int i = 0; i < U.length-1; i++)
			U[i] = 0;
		U[U.length-1] = (char)v;
	}
	
}
