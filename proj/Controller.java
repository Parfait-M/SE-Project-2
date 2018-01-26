package proj;

public class Controller {

	static Model model;
	static View view;
	static GameType game;

	static void exit() {
		System.out.println("Goodbye :)");
	}

	static void getGame() {
		// View.setGameType(game = GameType.SUDOKU);
		View.setGameType(game = GameType.NONOGRAM);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean quit = false;

		while(!quit) {
			//enum GameType has a quit value
			//getGame just asks the user for the game they want to play
			getGame();
			if(game == GameType.QUIT)
				exit(); // my function. Probably call view to print quitting message or not.

			//void function to take an enum GameType and make a board out of it. Guarentee it is not quit
			model.makeBoard(game);

			//Print the game board from model, passed in game to make it easier to know what to print
			View.displayBoard(model.getBoard(), model.getPrompt());

			//if we take input as strings, then:
			//getMove() just asks user for input. Returns string
			String input = view.getMove();
			while(!input.equalsIgnoreCase("quit") && !input.equalsIgnoreCase("q") &&
					!input.equalsIgnoreCase("exit")) {
				//QUIT can be whatever strings we decide to make game stop. I'll write that

				//updateBoard takes in a string value, and updates the game board based on that value.
				model.updateBoard(input);
				view.displayBoard(model.getBoard(),model.getPrompt()); // print board again
				input = view.getMove();
			}
		}
	}
}
