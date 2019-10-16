
public class TreasureIsland {
	
	public static final int IMPASS = 100000;
	public static final int NULL = 'N';
	public static final char START = 'S';
	public static final char TREASURE = 'X';
	public static final char HOLE = 'D';
	
	public static int[] findLandmarks(char[][] map) {
		int goalX = 0;
		int goalY = 0;
		// find the treasure coordinates
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] == TREASURE) {
					goalX = i;
					goalY = j;
				} else if (map[i][j] == START) {}
				
			}
		}
		return new int[]{goalX, goalY};
	}
	
	public static int[][] buildCostMap(char[][] map) {
		int[][] costMap = new int[map.length][map[0].length];
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				costMap[i][j] = NULL;
			}
		}
		
		// fill the costMap
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] != HOLE) {
					if (i == 0 && j == 0) {
						costMap[i][j] = 0;
					} else if (i == 0) {
						if (costMap[i][j - 1] < IMPASS)
							costMap[i][j] = 1 + costMap[i][j - 1];
					} else if (j == 0) {
						if (costMap[i - 1][j] < IMPASS)
							costMap[i][j] = 1 + costMap[i - 1][j];
					} else {
						if (Math.min(1 + costMap[i][j - 1], costMap[i - 1][j]) < IMPASS)
							costMap[i][j] = 1 + Math.min(1 + costMap[i][j - 1], costMap[i - 1][j]);
					}
				}
				else {
					costMap[i][j] = IMPASS;
				}
			}
		}
		costMap = checkForNull(costMap);
		return costMap;
	}
	
	private static int[][] checkForNull(int[][] costMap) {
		boolean nullFound = false; 
		for (int[] i : costMap) {
			for (int j : i) {
				if (j >= NULL && j < IMPASS) {
					nullFound = true;
				}
			}
		}
		if (nullFound) {
			for (int i = 0; i < costMap.length; i++) {
				for (int j = 0; j < costMap[i].length; j++) {
					if (costMap[i][j] >= NULL && costMap[i][j] < IMPASS) {
						if (i == costMap.length - 1 && j != costMap.length - 1) {
							if (costMap[i][j + 1] < IMPASS) {
								costMap[i][j] = 1 + costMap[i][j + 1];
							} else {
								costMap[i][j] = 1 + costMap[i - 1][j];
							}
						} else if (j == costMap[i].length - 1){
							if (costMap[i + 1][j] < IMPASS) {
								costMap[i][j] = 1 + costMap[i + 1][j];
							} else {
								costMap[i][j] = 1 + costMap[i][j - 1];
							}
						} else {
							costMap[i][j] = 1 + Math.min(1 + costMap[i][j + 1], 1 + costMap[i + 1][j]);
						}
					}
				}
			}
			return checkForNull(costMap);
		} else {
			return costMap;
		}
	}
	
	public static int[][] pathCoordinates(int[][] costMap, int[] treasureCoordinates) {
		int x = treasureCoordinates[0];
		int y = treasureCoordinates[1];	
		int[][] pathDesc = {{x, y}};
		int costLeft = costMap[x][y];
		
		pathDesc = pathRec(costMap, x, y, costLeft, pathDesc);
		
		int[][] pathAsc = new int[pathDesc.length][pathDesc[0].length];
		for (int i = 1; i <= pathDesc.length; i++) {
			for (int j = 0; j < pathDesc[0].length; j++) {
				pathAsc[pathAsc.length - i][j] = pathDesc[i - 1][j];
			}
		}
		return pathAsc;
	}
	
	public static int[][] pathRec(int[][] costMap, int x, int y, int costLeft, int[][] path) {
		
		int[][] temp = fillTemp(path);

		if (x == 0 && y == 0) {
			return path;
		}
		
		else if (y == 0) {
			if (costMap[x - 1][y] <= costMap[x][y] - 1) {
				temp[temp.length - 1][0] = x - 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x - 1, y, costLeft - 1, temp);
			} else if (costMap[x][y + 1] <= costMap[x][y] - 1) {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y + 1;
				return pathRec(costMap, x, y + 1, costLeft - 1, temp);
			} else {
				temp[temp.length - 1][0] = x + 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x + 1, y, costLeft - 1, temp);
			}
		} else if (x == 0) {
			if (costMap[x][y - 1] <= costMap[x][y] - 1) {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y - 1;
				return pathRec(costMap, x, y - 1, costLeft - 1, temp);
			} else if (costMap[x + 1][y] <= costMap[x][y] - 1) {
				temp[temp.length - 1][0] = x + 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x + 1, y, costLeft - 1, temp);
			} else {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y + 1;
				return pathRec(costMap, x, y + 1, costLeft - 1, temp);
			}
		} else if (x == costMap.length - 1 && y == costMap.length - 1) {
			int min = Math.min(costMap[x - 1][y], costMap[x][y - 1]);
			if (min == costMap[x - 1][y]) {
				temp[temp.length - 1][0] = x - 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x - 1, y, costLeft - 1, temp);
			} else {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y - 1;
				return pathRec(costMap, x, y - 1, costLeft - 1, temp);
			}
		} if (y == costMap.length - 1) {
			int min = Math.min(costMap[x - 1][y], costMap[x + 1][y]);
			min = Math.min(min, costMap[x][y - 1]);
			if (min == costMap[x - 1][y]) {
				temp[temp.length - 1][0] = x - 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x - 1, y, costLeft - 1, temp);
			} else if (min == costMap[x + 1][y]) {
				temp[temp.length - 1][0] = x + 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x + 1, y, costLeft - 1, temp);
			} else {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y - 1;
				return pathRec(costMap, x, y - 1, costLeft - 1, temp);
			}
		} else if (x == costMap.length - 1) {
			int min = Math.min(costMap[x][y - 1], costMap[x][y + 1]);
			min = Math.min(costMap[x - 1][y], min);
			if (min == costMap[x - 1][y]) {
				temp[temp.length - 1][0] = x - 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x - 1, y, costLeft - 1, temp);
			} else if (min == costMap[x][y - 1]) {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y - 1;
				return pathRec(costMap, x, y - 1, costLeft - 1, temp);
			} else {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y + 1;
				return pathRec(costMap, x, y + 1, costLeft - 1, temp);
			}
		} else {
			int min = Math.min(
				Math.min(costMap[x - 1][y], costMap[x + 1][y]),
				Math.min(costMap[x][y - 1], costMap[x][y + 1])
			);
			if (min == costMap[x - 1][y]) {
				temp[temp.length - 1][0] = x - 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x - 1, y, costLeft - 1, temp);
			} else if (min == costMap[x + 1][y]) {
				temp[temp.length - 1][0] = x + 1;
				temp[temp.length - 1][1] = y;
				return pathRec(costMap, x + 1, y, costLeft - 1, temp);
			} else if (min == costMap[x][y - 1]) {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y - 1;
				return pathRec(costMap, x, y - 1, costLeft - 1, temp);
			} else {
				temp[temp.length - 1][0] = x;
				temp[temp.length - 1][1] = y + 1;
				return pathRec(costMap, x, y + 1, costLeft - 1, temp);
			}
		}
	}
	
	private static int[][] fillTemp(int[][] path) {
		int[][] temp = new int[path.length + 1][2];
		for (int i = 0; i < path.length; i++) {
			for (int j = 0; j < path[0].length; j++) {
				temp[i][j] = path[i][j];
			}
		}
		return temp;
	}
	
	public static void main(String[] args) {
		char[][] map = {
				{'O', 'O', 'O', 'D', 'O'},
				{'D', 'D', 'O', 'O', 'O'},
				{'O', 'X', 'D', 'D', 'O'},
				{'O', 'D', 'D', 'O', 'O'},
				{'O', 'O', 'O', 'O', 'O'}
		};
		int[][] costMap = {};
		int[] treasureCoordinates = findLandmarks(map);
		try {
			costMap = buildCostMap(map);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Treasure is unreachable.");
			return;
		}
		
		
		for (int[] i : pathCoordinates(costMap, treasureCoordinates)) {
			System.out.print("( ");
			for (int j : i) {
				System.out.print(j + " ");
			}
			System.out.println(")");
		}
		
	}
	
}
