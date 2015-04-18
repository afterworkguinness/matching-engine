Feature: Order designation

	Scenario: A fill or kill market order and there is not enough liquidity
	Given the order book looks like this before the trade is placed
	      | side | quantity | limitPrice |
	      | sell  | 100      | 200        |
	When a Fill_or_Kill market order is placed to buy 300 shares
	Then the order should not be executed; the buy side of the order book should look like this: 
	      | side | quantity | limitPrice |
	
	And the sell side of the order book should look like this:
	      | side | quantity | limitPrice |
	      | sell  | 100      | 200        |