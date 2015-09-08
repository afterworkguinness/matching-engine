Feature: Execute order

  Scenario: A partial fill occurs and the trade is executed
    Given The order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
      | FOO    | buy  | 100      | 150        |
    When A limit sell order is placed for 200 shares of FOO at 150
    Then A trade for 100 shares of FOO should be executed at 150

  Scenario: A fill or kill order is palced and cannot be filled
    Given The order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
      | FOO    | buy  | 100      | 150        |
    When A Fill_or_Kill limit sell order is placed for 200 shares of FOO at 150
    Then No trades should be executed

  Scenario: A large trade is executed againt multiple resting orders
    Given The order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
      | FOO    | buy  | 100      | 150        |
      | FOO    | buy  | 120      | 150        |
      | FOO    | buy  | 130      | 90         |
    When A limit sell order is placed for 350 shares of FOO at 80
    Then the following trades are executed:
      | Symbol | TradePrice | quantity |
      | FOO    | 150        | 100      |
      | FOO    | 150        | 120      |
      | FOO    | 90         | 130      |
