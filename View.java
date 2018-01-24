
public class View
{
	
	private int boardSize;
	GameType gameType;
	
	// Adam/Zack: Input/Output
	
	// constructor
	View(int boardSize, GameType gameType)
	{
		this.boardSize = boardSize;
		this.gameType = gameType;
	}
	
	
	public String GetMove()
	{
		return "";
		// TODO get/validate user input.
	}
	
	public  void DisplayBoard(char[][] board)
	{
		if (gameType == GameType.NONOGRAM) 
		{
			DisplayNonogramBoard(board);
		}
		else if (gameType == GameType.OTHER)
		{
			Display_OTHERGAME_Board(board);
		}

	}
	
	private void Display_OTHERGAME_Board(char[][] board)
	{
		// TODO Auto-generated method stub
		
	}



	private void DisplayNonogramBoard(char[][] board)
	{
		// TODO Auto-generated method stub
		
	}



	public void PrintMessage(String message)
	{
		
	}
	
	
	// generates prompt for Nonogram game
	// receives a 5x5 board
	// returns a 10x5 array (2 5x5 arrays)
	private int[][] generateNongramPrompt(char[][] board)
	{
		  int colCounter = 0;	// counters for consecutive #'s
		  int rowCounter = 0;
		  int[][] prompt;	// array to store prompts
		  int promptI = 0;	// row index for prompt
		  int promptJ1 = 0;	// col indexes for prompt
		  int promptJ2 = 0;

		  // initialize prompt
		  prompt = new int[boardSize*2][];
		  for (int i = 0; i < boardSize*2; ++i) {
			  prompt[i] = new int[boardSize];
			  // initialize all elements to -1
			  for (int j = 0; j < boardSize; ++j) {
				  prompt[i][j] = -1;
			  }
		  }

		  // generate prompt
		  for (int i = 0; i < boardSize; ++i) {
			  for (int j = 0; j < boardSize; ++j) {

				  // check columns
				  if (board[j][i] == '#') {
					  colCounter++;
				  }
				  if ((board[j][i] == '_' || j == boardSize-1) && colCounter != 0) {
					  prompt[promptI][promptJ1] = colCounter;
					  promptJ1++;
					  colCounter = 0;
				  }

				  // check rows
				  if (board[i][j] == '#') {
					  rowCounter++;
				  }
				  if ((board[i][j] == '_' || j == boardSize-1) && rowCounter != 0) {
					  prompt[promptI+5][promptJ2] = rowCounter;
					  promptJ2++;
					  rowCounter = 0;
				  }
			  }
			  promptI++;
			  promptJ1 = promptJ2 = 0;
		  }

		  return prompt;
	}
	
}
