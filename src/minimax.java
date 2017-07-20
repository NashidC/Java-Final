import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Point;
import java.util.*;

class State implements Cloneable
{

	int rows, columns;
	char[][] gameBoard;

	//basics
	public State(int rowNum, int rowColumn){
		this.rows=rowNum;
		this.columns=rowColumn;
		this.gameBoard=new char[rowNum][rowColumn];
		
		
		for(int i=0; i<rowNum; i++)
			for(int j=0; j<rowColumn; j++)
				this.gameBoard[i][j]='.';
		//will fill up the board with blank spaces like no one has taken their turn yet
	}
	
	public boolean equals(Object obj){
		
		State other=(State)obj;
		return Arrays.deepEquals(this.gameBoard, other.gameBoard);
	}
	
	public int hashCode(){
		String b="";
		for(int i=0; i<gameBoard.length; i++)
			b+=String.valueOf(gameBoard[0]);
		return b.hashCode();
	}

	public Object clone() throws CloneNotSupportedException {
        State newState=new State(this.rows, this.columns);
		for (int i=0; i<this.rows; i++)
			newState.gameBoard[i] = (char[]) this.gameBoard[i].clone();
		return newState;
	}
	
	public ArrayList<Integer> getLegalActions(){
		ArrayList<Integer> moves=new ArrayList<Integer>();
		for(int j=0; j<this.columns; j++)
			if(this.gameBoard[0][j]=='.')
				moves.add(j);
		return moves;
	}
	

	public State generateSuccessor(char agent, int action) throws CloneNotSupportedException{
		
		int row;
		for(row=0; row<this.rows && this.gameBoard[row][action]!='X' && this.gameBoard[row][action]!='O'; row++);
		State new_state=(State)this.clone();
		new_state.gameBoard[row-1][action]=agent;
		
		return new_state;
	}
	
	
	public void printBoard(){
		System.out.println(new String(new char[this.columns*2]).replace('\0', '*'));
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<this.columns; j++){
				System.out.print(this.gameBoard[i][j]+" ");
			}
			System.out.println();
		}	
		System.out.println(new String(new char[this.columns*2]).replace('\0', '*'));
	}
	

	public boolean isWin(char agent){
	
		String find=""+agent+""+agent+""+agent+""+agent;
		
	
		for(int i=0; i<this.rows; i++)
			if(String.valueOf(this.gameBoard[i]).contains(find))
				return true;
		
	
		for(int j=0; j<this.columns; j++){
			String col="";
			for(int i=0; i<this.rows; i++)
				col+=this.gameBoard[i][j];
				
			if(col.contains(find))
				return true;
		}
		
		ArrayList<Point> move_Right = new ArrayList<Point>();
		ArrayList<Point> move_Left = new ArrayList<Point>();
		
		for(int j=0; j<this.columns-4+1; j++)
			move_Right.add(new Point(0,j));
		for(int j=4-1; j<this.columns; j++)
			move_Left.add(new Point(0,j));	
		for(int i=1; i<this.rows-4+1; i++){
			move_Right.add(new Point(i,0));
			move_Left.add(new Point(i,this.columns-1));
		}
	
		for (Point p : move_Right) {
			String d="";
			int x=p.x, y=p.y;
			while(true){				
				if (x>=this.rows||y>=this.columns)
					break;
				d+=this.gameBoard[x][y];
				x+=1; y+=1;
			}
			if(d.contains(find))
				return true;
		}
		
		for (Point p : move_Left) {
			String d="";
			int x=p.x, y=p.y;
			while(true){
				if(y<0||x>=this.rows||y>=this.columns)
					break;
				d+=this.gameBoard[x][y];
				x+=1; y-=1;
			}
			if(d.contains(find))
				return true;
		}
		
		return false;
		
	}
	
	
	public double evaluationFunction(){
	
		if (this.isWin('O'))
			return 1000.0;
		if (this.isWin('X'))
			return -1000.0;
		
		return 0.0;
	}

}

class minimaxAgent{
	
	int depth;
	int x=0;
	public minimaxAgent(int depth)
	{
		this.depth = depth;
	}	
	
	public int getAction(State st) throws CloneNotSupportedException
	{
		double val = max_value(st, depth);

		return x;
		
	}
	
	public double max_value(State st, int d) throws CloneNotSupportedException
	{
		ArrayList<Integer> children = new ArrayList<Integer>();
		if(d ==0)
		return st.evaluationFunction();
		else{
		children = st.getLegalActions();
		double v = -10000000;
		
		double z;
		
		for(int i =0; i<children.size();i++)
		{
			z = min_value(st.generateSuccessor('O',children.get(i)),d);
			if(z >= v)
			{
				v =z;
				this.x = i;
			}
		}

		return v;
		}
	}
	
	public double min_value(State st, int d) throws CloneNotSupportedException
	{
		
		ArrayList<Integer> children = new ArrayList<Integer>();
		if(d == 0)
		return st.evaluationFunction();
		else
		{
		children = st.getLegalActions();
		
		double v = 10000000;
		x = 0;
		double z;
		for(int i =0; i<children.size();i++)
		{
			z= max_value(st.generateSuccessor('X',children.get(i)),d-1);
			if(z <= v)
				v=z;
		
		}
		return v;
		}
	}
	
	
	
}

public class minimax{

	private static Scanner in;

	public static void main(String[] args) throws CloneNotSupportedException{
	
	System.out.println("Enter the depth:");
	in = new Scanner(System.in);
	int depth = in.nextInt();
	
		
	minimaxAgent mma = new minimaxAgent(depth);
	State s=new State(6,7);
	while(true){
		int action = mma.getAction(s);
		s = s.generateSuccessor('O', action);
		s.printBoard();
		if(s.isWin('O'))
		break;
		int enemy_move = in.nextInt();
		s = s.generateSuccessor('X', enemy_move);
		s.printBoard();

		if(s.isWin('X'))
		break;

		}
	}
}

