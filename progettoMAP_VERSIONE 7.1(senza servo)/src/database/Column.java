package database;
/**
 * Modella lo schema di una colonna all'interno di una tabella del database
 *
 */
public class Column {
		private String name;
		private String type;
		/**
		 * @param name 	nome della colonna
		 * @param type 		tipo della colonna 
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}
		public String getColumnName(){
			return name;
		}
		public boolean isNumber(){
			return type.equals("number");
		}
		public String toString(){
			return name+":"+type;
		}
	}

