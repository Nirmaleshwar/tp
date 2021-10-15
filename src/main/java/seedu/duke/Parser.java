package seedu.duke;

import seedu.commands.*;

import seedu.entry.Expense;
import seedu.entry.Income;

import seedu.exceptions.InvalidExpenseException;
import seedu.exceptions.InvalidExpenseIndexException;
import seedu.exceptions.InvalidIncomeException;
import seedu.exceptions.InvalidIncomeIndexException;
import seedu.utility.Messages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                    + " a/(?<amount>[^/]+)"
                    + " c/(?<category>[^/]+)");

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private static final Pattern ADD_INCOME_ARGUMENT_FORMAT =
            Pattern.compile("d/(?<description>[^/]+)"
                    + " a/(?<amount>[^/]+)"
                    + " c/(?<category>[^/]+)");

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

    private static final Pattern DATE_RANGE_ARGUMENT_FORMAT =
            Pattern.compile("s/(?<start>[^/]+)"
                    + "e/(?<end>[^/]+)");
    
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

    private static final String EXPENSE_RANGE_KEYWORD = "btw_ex";
    private static final String INCOME_RANGE_KEYWORD = "btw_in";
    
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
            return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments"); 

        switch (commandWord) {
        case HELP_COMMAND_KEYWORD:
            return prepareHelp(arguments);
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
        case EXPENSE_RANGE_KEYWORD:
            return prepareExpenseRange(arguments);
        case INCOME_RANGE_KEYWORD:
            return prepareIncomeRange(arguments);
        default:
            return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
        }
    }

    private Command prepareIncomeRange(String arguments) {
        final Matcher matcher = DATE_RANGE_ARGUMENT_FORMAT.matcher(arguments.trim());
        if (!matcher.matches()) {
            return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
        }
        try {
            String start = matcher.group("start").trim();
            LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String end = matcher.group("end").trim();
            LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return new TotalIncomeBetweenCommand(startDate,endDate);
        } catch (DateTimeParseException e) {
            return new InvalidCommand(Messages.DATE_FORMAT_MESSAGE);
        }
    }
    
    private Command prepareExpenseRange(String arguments) {
        final Matcher matcher = DATE_RANGE_ARGUMENT_FORMAT.matcher(arguments.trim());
        if (!matcher.matches()) {
            return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
        }
        try {
            String start = matcher.group("start").trim();
            LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String end = matcher.group("end").trim();
            LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return new TotalExpenseBetweenCommand(startDate,endDate);
        } catch (DateTimeParseException e) {
            return new InvalidCommand(Messages.DATE_FORMAT_MESSAGE);
        }
    }
    
    private Command prepareHelp(String arguments) {
        if (arguments.trim().isBlank()) {
            return new HelpCommand();
        }
        return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
    }

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private Command prepareAddExpense(String arguments) {
        final Matcher matcher = ADD_EXPENSE_ARGUMENT_FORMAT.matcher(arguments.trim());
        if (!matcher.matches()) {
            return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
        }
        
        String userGivenAmount = matcher.group("amount").trim();
        double expenseAmount;
        try {
            expenseAmount = parseExpense(userGivenAmount);
        } catch (InvalidExpenseException e) {
            return new InvalidCommand(e.getMessage());
        }
        assert expenseAmount > 0;

        String expenseDescription = matcher.group("description").trim();
        if (expenseDescription.isBlank()) {
            return new InvalidCommand(Messages.BLANK_DESCRIPTION_MESSAGE);
        }
        
        String expenseCategory = matcher.group("category").trim().toUpperCase();
        if (expenseCategory.isBlank()) {
            return new InvalidCommand(Messages.BLANK_CATEGORY_MESSAGE);
        }
        Expense expense = new Expense(expenseDescription, expenseAmount, expenseCategory);
        return new AddExpenseCommand(expense);
    }

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private Command prepareAddIncome(String arguments) {
        final Matcher matcher = ADD_INCOME_ARGUMENT_FORMAT.matcher(arguments.trim());
        if (!matcher.matches()) {
            return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
        }

        String userGivenAmount = matcher.group("amount").trim();
        double incomeAmount;
        try {
            incomeAmount = parseIncome(userGivenAmount);
        } catch (InvalidIncomeException e) {
            return new InvalidCommand(e.getMessage());
        }
        assert incomeAmount > 0;

        String incomeDescription = matcher.group("description").trim();
        if (incomeDescription.isBlank()) {
            return new InvalidCommand(Messages.BLANK_DESCRIPTION_MESSAGE);
        }

        String incomeCategory = matcher.group("category").trim().toUpperCase();
        if (incomeCategory.isBlank()) {
            return new InvalidCommand(Messages.BLANK_CATEGORY_MESSAGE);
        }
        Income income = new Income(incomeDescription, incomeAmount, incomeCategory);
        return new AddIncomeCommand(income);
    }

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private Command prepareDeleteExpense(String arguments) {
        final Matcher matcher = DELETE_EXPENSE_ARGUMENT_FORMAT.matcher(arguments.trim());
        if (!matcher.matches()) {
            return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
        }
        
        String userGivenIndex = matcher.group("index").trim();
        int deleteExpenseIndex;
        try {
            deleteExpenseIndex = parseExpenseIndex(userGivenIndex);
        } catch (InvalidExpenseIndexException e) {
            return new InvalidCommand(e.getMessage());
        }
        assert deleteExpenseIndex >= 1;
        
        return new DeleteExpenseCommand(deleteExpenseIndex);
    }

    /**
     * This was adapted from addressbook-level2 source code here:
     * https://github.com/se-edu/addressbook-level2/blob/master/src/seedu/addressbook/parser/Parser.java
     */
    private Command prepareDeleteIncome(String arguments) {
        final Matcher matcher = DELETE_INCOME_ARGUMENT_FORMAT.matcher(arguments.trim());
        if (!matcher.matches()) {
            return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
        }

        String userGivenIndex = matcher.group("index").trim();
        int deleteIncomeIndex;
        try {
            deleteIncomeIndex = parseIncomeIndex(userGivenIndex);
        } catch (InvalidIncomeIndexException e) {
            return new InvalidCommand(e.getMessage());
        }
        assert deleteIncomeIndex >= 1;
        
        return new DeleteIncomeCommand(deleteIncomeIndex);
    }

    private Command prepareListExpense(String arguments) {
        if (arguments.trim().isBlank()) {
            return new ListExpenseCommand();
        } 
        return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
    }

    private Command prepareListIncome(String arguments) {
        if (arguments.trim().isBlank()) {
            return new ListIncomeCommand();
        }
        return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
    }
    
    private Command prepareTotalExpense(String arguments) {
        if (arguments.trim().isBlank()) {
            return new TotalExpenseCommand();
        }
        return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
    }

    private Command prepareTotalIncome(String arguments) {
        if (arguments.trim().isBlank()) {
            return new TotalIncomeCommand();
        }
        return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
    }
    
    private Command prepareExit(String arguments) {
        if (arguments.trim().isBlank()) {
            return new ExitCommand();
        }
        return new InvalidCommand(Messages.INVALID_COMMAND_MESSAGE);
    }
    
    private double parseExpense(String userGivenAmount) throws InvalidExpenseException {
        double expenseAmount;
        try {
            expenseAmount = Double.parseDouble(userGivenAmount);
        } catch (NumberFormatException e) {
            throw new InvalidExpenseException(Messages.NON_NUMERIC_AMOUNT_MESSAGE);
        }
        if (expenseAmount <= 0) {
            throw new InvalidExpenseException(Messages.NON_POSITIVE_AMOUNT_MESSAGE);
        }
        return expenseAmount;
    }

    private double parseIncome(String userGivenAmount) throws InvalidIncomeException {
        double incomeAmount;
        try {
            incomeAmount = Double.parseDouble(userGivenAmount);
        } catch (NumberFormatException e) {
            throw new InvalidIncomeException(Messages.NON_NUMERIC_AMOUNT_MESSAGE);
        }
        if (incomeAmount <= 0) {
            throw new InvalidIncomeException(Messages.NON_POSITIVE_AMOUNT_MESSAGE);
        }
        return incomeAmount;
    }

    private int parseExpenseIndex(String userGivenIndex) throws InvalidExpenseIndexException {
        int deleteExpenseIndex;
        try {
            deleteExpenseIndex = Integer.parseInt(userGivenIndex);
        } catch (NumberFormatException e) {
            throw new InvalidExpenseIndexException(Messages.N0N_NUMERIC_INDEX_MESSAGE);
        }
        if (deleteExpenseIndex <= 0) {
            throw new InvalidExpenseIndexException(Messages.NON_POSITIVE_INDEX_MESSAGE);
        }
        return deleteExpenseIndex;
    }

    private int parseIncomeIndex(String userGivenIndex) throws InvalidIncomeIndexException {
        int deleteIncomeIndex;
        try {
            deleteIncomeIndex = Integer.parseInt(userGivenIndex);
        } catch (NumberFormatException e) {
            throw new InvalidIncomeIndexException(Messages.N0N_NUMERIC_INDEX_MESSAGE);
        }
        if (deleteIncomeIndex <= 0) {
            throw new InvalidIncomeIndexException(Messages.NON_POSITIVE_INDEX_MESSAGE);
        }
        return deleteIncomeIndex;
    }
}
