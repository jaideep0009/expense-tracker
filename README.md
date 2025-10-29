# expense-tracker
An Expense Tracking Desktop Application built using Java Swing, following clean architecture with Model-Service-Storage layers.
This application allows users to add income & expenses, categorize them, and view financial reports (Daily/Monthly/Yearly) with a user-friendly GUI.

Feature                                                  	Description

Add Income/Expense	                              Add transactions with amount, category, date & optional note
Manual Date Selection          	                  Popup calendar (JDatePicker)
JSON Storage	                                    Data stored locally in expenses.json
View History	                                    Full transaction list sorted latest first
Reports	Daily / Monthly / Yearly                  financial summary
Light Theme UI	                                  Clean Swing interface, fixed layout
MVC Structure                                   	Separation of concerns for maintainability

> ExpenseTracker
 > src/main/java/
   > model/              # Data model (Transaction.java)
   > service/            # Business logic (ExpenseService.java)
     > storage/            # Persistence (JsonStorage.java)
     > ui/                 # GUI (Swing-based)
         > MainDashboard.java
         > AddTransactionUI.java
         > TransactionHistoryUI.java
         > ReportUI.java

> expenses.json           # Auto-created local data storage
> pom.xml                 # Maven dependencies

 Category             Technology                     
 
 Language             Java                  
 GUI                  Java Swing                     
 Calendar Picker      JDatePicker           
 Storage              JSON (org.json library)        
 Build Tool           Maven                          
 Architecture         MVC  
