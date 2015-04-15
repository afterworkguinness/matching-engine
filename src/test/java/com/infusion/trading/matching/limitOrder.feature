Feature: Limit Order

	Scenario: A limit order is executed
	   Given: The order book looks likes this:
		      | side	| quantity	| limitPrice	|
		      | buy		| 100		| 200			|
	    When: A limit sell order is placed for 100 shares at 200
	    Then: The order book should look like this at the end of the trade:
		      | side		| quantity	| limitPrice	|
