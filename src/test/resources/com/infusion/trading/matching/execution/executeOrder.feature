Feature: Execute order

Scenario: A partial fill occurs and the trade is executed
    Given The order book looks like this before the trade is placed:
      | side | quantity | limitPrice |
      |	buy	 | 100		| 150		 |
    When A limit sell order is placed for 200 shares at 150
    Then A trade for 100 shares should be executed at 150