package ua.ryabuhin_valentine;

public class Main {

	public static void main(String[] args) {
		/* Check the command-line options */
		String wayIn = null, wayOut = null;
		try {
			if (args[0].equals("-i") && args[2].equals("-o")) {
				wayIn = new String(args[1]);
				wayOut = new String(args[3]);
			} else if (args[0].equals("-o") && args[2].equals("-i")) {
				wayIn = new String(args[3]);
				wayOut = new String(args[1]);
			} else {
				System.out.println("Unknown commands ...");
				System.exit(-1);
			}
		} catch (Exception e) {
			System.out.println("Please, enter parameter values \"-i\" and \"-o\"");
			System.exit(-1);
		}
		/* Go :) */
		GistImageUtil util = new GistImageUtil(wayIn, wayOut);
		util.run();

	}
}
