Feature: Limit Order

	Scenario: A limit order is executed
	   Given The order book looks like this before the trade is placed:
		      | side	| quantity	| limitPrice	|
		      | buy		| 100		| 200			|
	    When A limit sell order is placed for 100 shares at 200
	    Then The buy side of the order book should look like this at the end of the trade:
		      | side		| quantity	| limitPrice	|
		      

    Scenario: A limit order is executed and runs out of liquidity
        Given The order book looks like this before the trade is placed:
		      | side	| quantity	| limitPrice	|
		      | sell	| 100		| 200			|
		 When A limit buy order is placed for 150 shares at 200
		 Then The sell side of the order book should look like this at the end of the trade:
 		      | side	| quantity	| limitPrice	|
	      And The buy side of the order book should look like this at the end of the trade:
 		      | side	| quantity	| limitPrice	|
 		      | buy		| 50		| 200			|