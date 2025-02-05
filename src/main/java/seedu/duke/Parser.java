package seedu.duke;

import seedu.commands.AddExpenseCommand;
import seedu.commands.AddIncomeCommand;
import seedu.commands.ListExpenseCommand;
import seedu.commands.ListIncomeCommand;
import seedu.commands.DeleteExpenseCommand;
import seedu.commands.DeleteIncomeCommand;
import seedu.commands.InvalidCommand;
import seedu.commands.HelpCommand;
import seedu.commands.TotalExpenseCommand;
import seedu.commands.TotalIncomeCommand;
import seedu.commands.Command;
import seedu.commands.ExitCommand;

import seedu.entry.Expense;
import seedu.entry.Income;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    /**
     * Used for initial separation of command word and args.
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private static final Pattern ADD_EXPENSE_ARGUMENT_FORMAT =
            Pattern.compile("d/(?<description>[^/]+)"
                    + " a/(?<amount>[^/]+)");

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private static final Pattern ADD_INCOME_ARGUMENT_FORMAT =
            Pattern.compile("d/(?<description>[^/]+)"
                    + " a/(?<amount>[^/]+)");

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private static final Pattern DELETE_EXPENSE_ARGUMENT_FORMAT =
            Pattern.compile("i/(?<index>[^/]+)");

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private static final Pattern DELETE_INCOME_ARGUMENT_FORMAT =
            Pattern.compile("i/(?<index>[^/]+)");
    
    private static final String HELP_COMMAND_KEYWORD = "help";
    private static final String ADD_EXPENSE_KEYWORD = "add_ex";
    private static final String ADD_INCOME_KEYWORD = "add_in";
    private static final String DELETE_EXPENSE_KEYWORD = "del_ex";
    private static final String DELETE_INCOME_KEYWORD = "del_in";
    private static final String LIST_EXPENSE_KEYWORD = "list_ex";
    private static final String LIST_INCOME_KEYWORD = "list_in";
    private static final String TOTAL_EXPENSE_KEYWORD = "total_ex";
    private static final String TOTAL_INCOME_KEYWORD = "total_in";
    private static final String EXIT_KEYWORD = "end";

    /**
     * Parses user input into command for execution.
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new InvalidCommand();
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        switch (commandWord) {
        case HELP_COMMAND_KEYWORD:
            return new HelpCommand();
        case ADD_EXPENSE_KEYWORD:
            return prepareAddExpense(arguments);
        case ADD_INCOME_KEYWORD:
            return prepareAddIncome(arguments);
        case DELETE_EXPENSE_KEYWORD:
            return prepareDeleteExpense(arguments);
        case DELETE_INCOME_KEYWORD:
            return prepareDeleteIncome(arguments);
        case LIST_EXPENSE_KEYWORD:
            return prepareListExpense(arguments);
        case LIST_INCOME_KEYWORD:
            return prepareListIncome(arguments);
        case TOTAL_EXPENSE_KEYWORD:
            return prepareTotalExpense(arguments);
        case TOTAL_INCOME_KEYWORD:
            return prepareTotalIncome(arguments);
        case EXIT_KEYWORD:
            return prepareExit(arguments);
        default:
            return new InvalidCommand();
        }
    }

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private Command prepareAddExpense(String arguments) {
        final Matcher matcher = ADD_EXPENSE_ARGUMENT_FORMAT.matcher(arguments.trim());

        if (!matcher.matches()) {
            return new InvalidCommand();
        }
        
        String expenseDescription = matcher.group("description").trim();
        int expenseAmount;
        
        try {
            expenseAmount = Integer.parseInt(matcher.group("amount"));
        } catch (NumberFormatException e) {
            return new InvalidCommand();
        }
        
        //need to create constructor for Expense
        Expense expense = new Expense();
        return new AddExpenseCommand(expense);
    }

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private Command prepareAddIncome(String arguments) {
        final Matcher matcher = ADD_INCOME_ARGUMENT_FORMAT.matcher(arguments.trim());

        if (!matcher.matches()) {
            return new InvalidCommand();
        }

        String incomeDescription = matcher.group("description").trim();
        int incomeAmount;

        try {
            incomeAmount = Integer.parseInt(matcher.group("amount"));
        } catch (NumberFormatException e) {
            return new InvalidCommand();
        }
        
        //need to add the constructor for Income
        Income income = new Income();
        return new AddIncomeCommand(income);
    }

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private Command prepareDeleteExpense(String arguments) {
        final Matcher matcher = DELETE_EXPENSE_ARGUMENT_FORMAT.matcher(arguments.trim());

        if (!matcher.matches()) {
            return new InvalidCommand();
        }
        
        try {
            int deleteIndex = Integer.parseInt(matcher.group("index"));
            return new DeleteExpenseCommand(deleteIndex);
        } catch (NumberFormatException e) {
            return new InvalidCommand();
        }
    }

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private Command prepareDeleteIncome(String arguments) {
        final Matcher matcher = DELETE_INCOME_ARGUMENT_FORMAT.matcher(arguments.trim());

        if (!matcher.matches()) {
            return new InvalidCommand();
        }

        try {
            int deleteIndex = Integer.parseInt(matcher.group("index"));
            return new DeleteIncomeCommand(deleteIndex);
        } catch (NumberFormatException e) {
            return new InvalidCommand();
        }
    }

    private Command prepareListExpense(String arguments) {
        if (arguments.trim().isBlank()) {
            return new ListExpenseCommand();
        } 
        return new InvalidCommand();
    }

    private Command prepareListIncome(String arguments) {
        if (arguments.trim().isBlank()) {
            return new ListIncomeCommand();
        }
        return new InvalidCommand();
    }
    
    private Command prepareTotalExpense(String arguments) {
        if (arguments.trim().isBlank()) {
            return new TotalExpenseCommand();
        }
        return new InvalidCommand();
    }

    private Command prepareTotalIncome(String arguments) {
        if (arguments.trim().isBlank()) {
            return new TotalIncomeCommand();
        }
        return new InvalidCommand();
    }
    
    private Command prepareExit(String arguments) {
        if (arguments.trim().isBlank()) {
            return new ExitCommand();
        }
        return new InvalidCommand();
    }
}
