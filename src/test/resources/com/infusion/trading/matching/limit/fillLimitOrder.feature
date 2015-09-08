Feature: Limit Order

  Scenario: A limit order is added to an empty order book
    Given The order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
    When A limit buy order is placed for 100 shares of FOO at 50
    Then The order book should look like this at the end of the trade:
      | symbol  | side | quantity | limitPrice |
      | FOO		| buy  | 100      | 50         |


  Scenario: A limit order is executed
    Given The order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
      | FOO    | buy  | 100      | 200        |
    When A limit sell order is placed for 100 shares of FOO at 200
    Then The order book should look like this at the end of the trade:
      | symbol | side | quantity | limitPrice |
      | FOO    |  	  |     	 |     		  |

  Scenario: A limit order is executed and doesn't touch the resting limit order for another stock
    Given The order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
      | FOO    | buy  | 100      | 200        |
      | BAR	   | buy  | 100		 | 500		  |
    When A limit sell order is placed for 100 shares of FOO at 200
    Then The order book should look like this at the end of the trade:
      | symbol | side | quantity | limitPrice |
	  | BAR	   | buy  | 100		 | 500		  |	
	  | FOO	   |  	  | 		 | 			  |	

  Scenario: A limit order is partially filled against one resting order and runs out of liquidity
    Given The order book looks like this before the trade is placed:
      | symbol	| side | quantity | limitPrice |
      | FOO		| sell | 100      | 200        |
    When A limit buy order is placed for 150 shares of FOO at 200
    Then The order book should look like this at the end of the trade:
      | symbol	| side	| quantity	| limitPrice	|
      | FOO		|		|			|				|
    And The order book should look like this at the end of the trade:
  	  | symbol	| side | quantity | limitPrice |
      | FOO		| buy  | 50       | 200        |

  Scenario: A limit order is partially filled against multiple resting orders and runs out of liquidity
    Given The order book looks like this before the trade is placed:
      |	symbol	| side | quantity | limitPrice |
      | FOO		| sell | 100      | 200        |
      | FOO		| sell | 150      | 200        |
    When A limit buy order is placed for 300 shares of FOO at 200
    Then The order book should look like this at the end of the trade:
      |	symbol	| side	| quantity	| limitPrice	|
      | FOO		| buy	| 50       	| 200        	|
      
  Scenario: A limit sell order crosses the bid ask spread
    Given The order book looks like this before the trade is placed:
      | side | quantity | limitPrice |
      | sell | 100      | 200        |
      | buy  | 100      | 150        |
    When A limit sell order is placed for 100 shares at 145
    Then It crosses the bid ask spread and is executed at 150
    And The sell side of the order book should look like this at the end of the trade:
      | side | quantity | limitPrice |
      | sell | 100      | 200        |
    And The buy side of the order book should look like this at the end of the trade:
      | side | quantity | limitPrice |
