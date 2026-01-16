# Accounting Ledger Application
## Application features
Home menu:
- (L) View the main ledger screen
- (D) Add deposit
- (P) Make payment
- (X) Exit the application

Ledger View:
- (A) Display All transactions from newest to oldest
- (D) HomeScreen  only deposits
- (P) HomeScreen only payments
- (R) Reports menu

Reports Menu:
- 1-4 - Pre-set financial reports (Month to Date, Previous Month, Year to Date, and Previous Year)
- 5 - Keyword search by Vendor Name
- 6 - Custom Search (filtered by: Date range, Description, Vendor, and Amount)

Screenshots:
1. ![Home Screen Menu](images/home_screen.)
2. ![All Transaction View](images/all_transactions.png)
3. ![Custom Search Results](images/custom_search.png)

Interesting Piece of Code:
Direct comparison of "double" values often leads to minor inaccuracies. To ensure the amounts are always accurate I used "Cents Check" to convert bot the transaction amount and the search amount into long integers (represented as cents) and compare those instead  

** if (searchAmount != null) { // Check amounts using cents **
   long transactionsCents = Math.round(t.getAmount() * 100); 
   long searchCents = Math.round(searchAmount * 100);
   if (transactionsCents != searchCents) {
   passesAllFilters = false;
   }