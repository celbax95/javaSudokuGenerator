package fr.generator;

import java.util.ArrayList;
import java.util.Random;

public class SudoGen {

	private static Random r = new Random();
	
	private static final int T = 9;
	private static final int Z = 3;

	private static final int FULL = T*T;
	private static final int[] MORESOL = {10,15};
	private static final int[] EASY = {40,45};
	private static final int[] MED = {35,40};
	private static final int[] HARD = {30,35};
	private static final int[] EXTR = {25,30};
	
	private boolean displayBuild;
	
	private int[][][] c;
	private int[][] cf;
	private ArrayList<int[][]> cff;
	
	SudoGen() {
		displayBuild = false;
		cff = new ArrayList<>();
		c = new int[T][T][T];
		cf = new int[T][T];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c.length; j++) {
				cf[j][i] = 0;
			}
		}
		resetBuilder();
	}
	
	public int[][] getEasy(int[][] t2) {
		return getPlayableBoard(t2,EASY);
	}
	public int[][] getMedium(int[][] t2) {
		return getPlayableBoard(t2,MED);
	}
	public int[][] getHard(int[][] t2) {
		return getPlayableBoard(t2,HARD);
	}
	public int[][] getExtreme(int[][] t2) {
		return getPlayableBoard(t2,EXTR);
	}
	
	public int[][] getPlayableBoard(int[][] t2, int[] diff) {
		int tmp = FULL-diff[0]-r.nextInt(diff[1]-diff[0]);
		int x,y;
		int[][] t;
		
		do {
			t = clone(t2);
			for (int i = 0; i < tmp; i++) {
				do {
					x = r.nextInt(T);
					y = r.nextInt(T);
				} while (t[x][y]==0);
				
				t[x][y] = 0;
			}			
		} while (!isUnique(t,t2));
		return t;
	}
	
	public boolean isUnique(int[][] t, int[][] full) {
		ArrayList<int[][]> a = getSol(t);
		if (a.size() == 1)
			return (areEquals(a.get(0),full));
		return false;
	}
	
	public ArrayList<int[][]> getSol(int[][] t2) {
		boolean end = false;
		int[][] t = clone(t2);
		int[][] last = null;
		int lastCpt = 0;
		ArrayList<int[][]> sol = new ArrayList<>();
		resetBuilder();
		
		int cpt=0,x,y,n;
		
		while (t2[cpt%T][cpt/T] != 0)
			cpt++;
		
		while (!end) {
			x = cpt%T; y = cpt/T;
			n = len(c[x][y]);
			
			if (n == 0) {
				reFull(c[x][y]);
				t[x][y] = 0;
				do {
					cpt--;
					if (cpt<0) {
						cpt = FULL-1; 
						break;
					}
				} while (t2[cpt%T][cpt/T] != 0);
				continue;
			}
			
			for (int i = 0; i < c[x][y].length; i++) {
				if (c[x][y][i] != 0)
					n = i;
			}
			
			if (!isAllowed(t,c[x][y][n],x,y)) {
				c[x][y][n] = 0;
				continue;
			}
			
			t[x][y] = c[x][y][n];
			c[x][y][n] = 0;
			do {
				cpt++;
				cpt%=FULL;
			} while (t2[cpt%T][cpt/T] != 0);
			
			if (isFull(t)) {
				if (sol.size() > 0) {
					if (lastCpt > sol.size()*10 || (!areEquals(last,t) && areEquals(t,sol.get(0)))) {
						end = true;
						continue;
					}					
				}
				if (!alreadyFound(sol, t)) {
					lastCpt = 0;
					last = clone(t);
					sol.add(clone(t));
					resetBuilder();
				}
				else {
					do {
						cpt++;
						cpt%=FULL;
					} while (t2[cpt%T][cpt/T] != 0);
					lastCpt++;
				}
			}
		}
		return sol;
	}
	
	public boolean alreadyFound(ArrayList<int[][]> a, int[][] t) {
		int tmp = a.size();
		for (int i = 0; i < tmp; i++) {
			if (areEquals(a.get(i),t))
				return true;
		}
		return false;
	}
	
	public boolean areEquals(int[][] t1 , int[][] t2) {
		return areEquals(t1,t2,false);
	}
	public boolean areEquals(int[][] t1 , int[][] t2, boolean display) {
		if (display)
			System.out.println();
		if (t1==null || t2 == null) {
			if (display)
				System.out.println("null found");
			return false;
		}
		if (t1.length != t2.length) {
			if (display)
				System.out.println("not same size");
			return false;
		}
		if (t1[0].length != t2[0].length) {
			if (display)
				System.out.println("not same size");
			return false;
		}
		for (int i = 0; i < t2.length; i++) {
			for (int j = 0; j < t2.length; j++) {
				if (t1[j][i] != t2[j][i]) {
					if (display)
						System.out.println(j+" "+i+" squares are differents ("+t1[j][i]+" "+t2[j][i]+")");
					return false;					
				}
			}
		}
		return true;
	}
	
	public void resetBuilder() {
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c.length; j++) {
				for (int j2 = 0; j2 < c.length; j2++) {
					c[j][i][j2] = j2+1;
				}
			}
		}
	}
	public void displayBuildOn() {
		displayBuild = true;
	}
	public void displayBuildOff() {
		displayBuild = false;
	}
	
	public int[][] create() throws InterruptedException {
		int cpt = 0,x,y,n;
		
		while (!isFull(cf)) {
			x = cpt%9; y = cpt/T;
			n = len(c[x][y]);
			
			if (displayBuild) {
				Thread.sleep(30);
				System.out.print(cpt + " | ");
				for (int i = 0; i < c.length; i++)
					System.out.print(" " + c[x][y][i]);
				System.out.println();
				System.out.println(this);				
			}
			
			if (n == 0) {
				reFull(c[x][y]);
				cf[x][y] = 0;
				cpt=cpt-1<0?FULL-1:cpt-1;
				continue;
			}
			n = random(c[x][y]);
			
			if (!isAllowed(c[x][y][n],x,y)) {
				c[x][y][n] = 0;
				continue;
			}
			
			cf[x][y] = c[x][y][n];
			c[x][y][n] = 0;
			cpt++;
		}
		return cf;
	}
	
	public int random(int[] t) {
		int n = r.nextInt(t.length);
		while(t[n] == 0)
			n=(n+1)%t.length;
		return n;
	}
	
	public String toString() {
		String s = "";
		s+="-----------------------------------------\n";
		for (int y = 0; y < c.length; y++) {
			for (int x = 0; x < c.length; x++) {
				if (x%3==0 && x!=0)
					s+=" ";
				s += " " + cf[x][y];
			}
			s+="\n";
			if ((y+1)%3==0 && y!=0)
				s+="\n";
		}
		s+="-----------------------------------------";
		return s;
	}
	
	public boolean isAllowed(int n, int x, int y) {
		return isAllowed(cf,n,x,y);
	}
	public boolean isAllowed(int[][] ref, int n, int x, int y) {
		return (!isInZone(ref,n,x,y) && !isInLine(ref,n,y) && !isInCol(ref,n,x));
	}
	
	public int len(int[] t) {
		int cpt = 0;
		for (int i = 0; i < t.length; i++) {
			if (t[i] != 0)
				cpt++;
		}
		return cpt;
	}
	public void reFull(int[] t) {
		for (int i = 0; i < t.length; i++) {
			t[i] = i+1;
		}
	}
	
	public boolean isFull(int[][] t) {
		for (int i = 0; i < T; i++) {
			for (int j = 0; j < T; j++) {
				if (t[j][i] == 0)
					return false;
			}
		}
		return true;
	}
	
	public boolean isInZone(int[][] ref, int n, int x, int y) {
		if (n==0) return false;
		int zx = (int)x/Z, zy = (int)y/Z;
		for (int i = zy*3; i < zy*3+3; i++) {
			for (int j = zx*3; j < zx*3+3; j++) {
				if (n == ref[j][i])
					return true;
			}
		}
		return false;
	}
	
	public boolean isInLine(int[][] ref, int n, int y) {
		if (n==0) return false;
		for (int i = 0; i < ref.length; i++) {
			if (ref[i][y] == n)
				return true;
		}
		return false;
	}
	
	public boolean isInCol(int[][] ref, int n, int x) {
		if (n==0) return false;
		for (int i = 0; i < ref.length; i++) {
			if (ref[x][i] == n)
				return true;
		}
		return false;
	}
	
	public int[][] clone(int[][] t2) {
		int[][] t = new int[t2.length][t2[0].length];
		for (int y = 0; y < t2.length; y++)
			for (int x = 0; x < t2.length; x++)
				t[x][y] = t2[x][y];
		return t;
	}
}
