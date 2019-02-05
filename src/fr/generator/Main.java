package fr.generator;

public class Main {

	
	public static void display(int[][] g) {
		String s = "";
		for (int y = 0; y < g[0].length; y++) {
			for (int x = 0; x < g.length; x++) {
				if (x%3==0 && x!=0)
					s+=" ";
				s += " " + (g[x][y]==0?"_":g[x][y]);
			}
			s+="\n";
			if ((y+1)%3==0 && y!=0)
				s+="\n";
		}
		s+="--";
		System.out.println(s);
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		SudoGen s = new SudoGen();
		//s.displayBuildOn();
		int[][] full = s.create();
		System.out.println("FULL GRID");
		display(full);
		
		int[][] easy = s.getEasy(full);
		int[][] med = s.getMedium(full);
		int[][] hard = s.getHard(full);
		int[][] extr = s.getExtreme(full);
		
		System.out.println("EASY");
		display(easy);
		System.out.println("MEDIUM");
		display(med);
		System.out.println("HARD");
		display(hard);
		System.out.println("EXTR");
		display(extr);
	}
}
