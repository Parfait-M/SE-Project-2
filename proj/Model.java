package proj;

import java.util.ArrayList;

public class Model
{

	private static int SIZE;
	private static GameType gameType;

	private static char[][] solved_board;
	private static char[][] current_board;

	// John: generate board

	public static void main(String[] a) {
		makeBoard(GameType.NONOGRAM);
		View.displayBoard(getBoard(),getPrompt());
		// makeBoard(GameType.SUDOKU);
	}

	static void makeBoard(GameType gt) {
		gameType = gt;

		if (gt == GameType.NONOGRAM) makeNonogram();
		else if (gt == GameType.SUDOKU) makeSudoku();
		else gameType = GameType.QUIT;
	}

	static char[][] getBoard() {
		if (gameType == GameType.NONOGRAM) return getNonogram();
		else if (gameType == GameType.SUDOKU) return getSudoku();
		else return null;
	}
	static char[][] getPrompt() {
		if (gameType == GameType.NONOGRAM)
			return solved_board;
		else return null;
	}

	// updates the board based on input
	static void updateBoard(String input) {

	}

	// Nonogram
	// ------------------------------------------------------------

	private static int[][] options(int total, int count) {
		if (count == 0) {
			int[][] ans = {};
			return ans;
		}
		else if (count == 1) {
			int[][] ans = {{total}};
			return ans;
		}
		else {
			ArrayList<int[]> ans = new ArrayList<int[]>();
			for (int i = 0; i <= total; ++i) {
				int[][] op = options(total - i, count - 1);

				for (int[] j : op) {
					int[] temp = new int[j.length + 1];
					temp[0] = i;
					for (int k = 0; k < j.length; ++k) {
						temp[k+1] = j[k];
					}
					ans.add(temp);
				}
			}
			return ans.toArray(new int[ans.size()][]);
		}
	}
	private static boolean[][] make_combos(int[] p) {
		int l = SIZE;
		int sum = 0;
		for (int i : p) {
			sum += i;
			l -= i == 0 ? 1 : 0;
		}

		int total = SIZE - (sum + l - 1);
		total = total > SIZE ? SIZE : total;

		int[][] ops = options(total, l + 1);
		ArrayList<boolean[]> ans = new ArrayList<boolean[]>();

		for (int[] op : ops) {
			boolean[] s = new boolean[SIZE];
			int k = 0;

			for (int i = 0; i < l; ++i) {
				for (int j = 0; j < op[i] + (i>0?1:0); ++j)
					s[k++] = false;
				for (int j = 0; j < p[i]; ++j)
					s[k++] = true;
			}
			for (int j = 0; j < op[l]; ++j)
				s[k++] = false;

			ans.add(s);
		}
		return ans.toArray(new boolean[ans.size()][]);
	}
	private static boolean[][][] get_mask(boolean[][][] a_combos, boolean[][][] b_combos) {
		boolean[][][] mask = new boolean[2][SIZE][SIZE];

		for (int a = 0; a < SIZE; ++a) {
			boolean[] sub_mask_0 = mask[0][a];
			boolean[] sub_mask_1 = mask[1][a];

			for (boolean[] a_combo : a_combos[a]) {
				if (a_combo == null) continue;

				for (int b = 0; b < SIZE; ++b) {
					(a_combo[b] ? sub_mask_0 : sub_mask_1)[b] = true;
				}
			}
		}
		return mask;
	}
	private static boolean cut_mask( boolean[][][] a_combos, boolean[][][] b_combos) {
		boolean change = false;
		boolean[][][] mask = get_mask(a_combos,b_combos);

		for (int b = 0; b < SIZE; ++b) {
			boolean[][] sub_b_combos = b_combos[b];

			for (int sb = 0; sb < sub_b_combos.length; ++sb) {
				boolean[] b_combo = sub_b_combos[sb];
				if (b_combo == null) continue;

				boolean fail = false;
				for (int a = 0; a < SIZE && !fail; ++a) {
					fail = !mask[b_combo[a] ? 0 : 1][a][b];
				}
				if (fail) {
					sub_b_combos[sb] = null;
					change = true;
				}
			}
		}

		return change;
	}

	private static void makeNonogram() {
		SIZE = 5;

		View.setGameType(GameType.NONOGRAM);
		View.setBoardSize(SIZE);

		boolean fail;
		do {
			char[][] board = new char[SIZE][SIZE];
			current_board = new char[SIZE][SIZE];

			for (int i = 0; i < SIZE; ++i)
				for (int j = 0; j < SIZE; ++j) {
					board[i][j] = Math.random() < 0.6 ? '#' : '_';
					current_board[i][j] = '_';
				}


			int[][] col_prompt = new int[SIZE][SIZE];
			int[][] row_prompt = new int[SIZE][SIZE];

			// get prompts
			for (int i = 0; i < SIZE; ++i) {
				int col_row_count = 0;
				int row_col_count = 0;
				int col_row_index = 0;
				int row_col_index = 0;

				for (int j = 0; j < SIZE; ++j) {
					if (board[i][j] == '#') {
						++col_row_count;
					}
					else if (col_row_count > 0){
						col_prompt[i][col_row_index++] = col_row_count;
						col_row_count = 0;
					}

					if (board[j][i] == '#') {
						++row_col_count;
					}
					else if (row_col_count > 0) {
						row_prompt[i][row_col_index++] = row_col_count;
						row_col_count = 0;
					}
				}

				if (col_row_count > 0)
					col_prompt[i][col_row_index++] = col_row_count;
				if (row_col_count > 0)
					row_prompt[i][row_col_index++] = row_col_count;
			}

			boolean[][][] col_combos = new boolean[SIZE][][];
			boolean[][][] row_combos = new boolean[SIZE][][];
			for (int i = 0; i < SIZE; ++i) {
				col_combos[i] = make_combos(col_prompt[i]);
				row_combos[i] = make_combos(row_prompt[i]);
			}

			do {
				cut_mask(row_combos, col_combos);
			} while (cut_mask(col_combos, row_combos));

			boolean[][][] mask = get_mask(col_combos,row_combos);
			fail = false;
			for (int i = 0; i < SIZE && !fail; ++i)
				for (int j = 0; j < SIZE && !fail; ++j)
					fail = mask[0][i][j] && mask[1][i][j];

			solved_board = board;
		} while (fail);
	}
	private static char[][] getNonogram() {
		// return solved_board;
		return current_board;
	}

	// Sudoku
	// ------------------------------------------------------------

	private static int prompt_index = 0;
	private static String[] sudoku_prompts = {
		"_____9__81_4___5__26__1_____9__3_4__81_7_5_36__3_9__8_____5__43__1___8_55__3_____",
		"____6___23__9__6___48_____5_79__2___46_____23___4__96_2_____35___1__6__85___4____",
		"_______74_8_4_3_9_____571__3________9_45_83_6________1__932_____2_8_9_6_13_______",
		"_9_6_2_8_______9__23_____64_1_49___745_____937___23_4_36_____79__1_______2_1_6_3_",
		"_5___4____97_8___51_____9___6___8__2__24_68__4__2___7___1_____97___4_15____9___3_"
	};

	private static void makeSudoku() {
		SIZE = 9;
		View.setBoardSize(SIZE);
		solved_board = new char[SIZE][SIZE];
		current_board = new char[SIZE][SIZE];

		char[] prompt = sudoku_prompts[prompt_index++ % sudoku_prompts.length].toCharArray();
		for (int i = 0; i < SIZE*SIZE; i += SIZE) {
			for (int j = 0; j < SIZE; ++j) {
				current_board[i][j] = solved_board[i][j] = prompt[i + j];
			}
		}


	}
	private static char[][] getSudoku() {
		return solved_board;
	}



}
