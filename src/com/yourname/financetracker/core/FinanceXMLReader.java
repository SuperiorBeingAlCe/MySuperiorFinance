package com.yourname.financetracker.core;


import org.w3c.dom.*;

import com.yourname.financetracker.model.Debt;
import com.yourname.financetracker.model.Expense;
import com.yourname.financetracker.model.Income;
import com.yourname.financetracker.service.FinanceManager;

import javax.xml.parsers.*;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
public class FinanceXMLReader {

    public static FinanceManager load() {
        FinanceManager manager = new FinanceManager();
        Path filePath = FinancePaths.getDataFilePath();

        File file = filePath.toFile();
        if (!file.exists()) {
            return manager; // ilk açılış
        }

        try {
            DocumentBuilder builder =	
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            // Incomes
            NodeList incomeNodes = doc.getElementsByTagName("income");
            for (int i = 0; i < incomeNodes.getLength(); i++) {
                Element e = (Element) incomeNodes.item(i);
                manager.addIncome(new Income(
                        Double.parseDouble(e.getAttribute("amount")),
                        LocalDate.parse(e.getAttribute("date")),
                        e.getElementsByTagName("description")
                         .item(0).getTextContent()
                ));
            }

            // Expenses
            NodeList expenseNodes = doc.getElementsByTagName("expense");
            for (int i = 0; i < expenseNodes.getLength(); i++) {
                Element e = (Element) expenseNodes.item(i);
                manager.addExpense(new Expense(
                        Double.parseDouble(e.getAttribute("amount")),
                        LocalDate.parse(e.getAttribute("date")),
                        e.getElementsByTagName("description")
                         .item(0).getTextContent()
                ));
            }

            // Debts
            NodeList debtNodes = doc.getElementsByTagName("debt");
            for (int i = 0; i < debtNodes.getLength(); i++) {
                Element e = (Element) debtNodes.item(i);

                Debt debt = new Debt(
                        Double.parseDouble(e.getAttribute("amount")),
                        LocalDate.parse(e.getAttribute("date")),
                        e.getElementsByTagName("description")
                         .item(0).getTextContent(),
                        LocalDate.parse(e.getAttribute("deadline"))
                );

                if (Boolean.parseBoolean(e.getAttribute("paid"))) {
                    debt.markAsPaid();
                }

                manager.addDebt(debt);
            }
            
            

        } catch (Exception e) {
            throw new RuntimeException("XML okunamadı", e);
        }

        return manager;
    }
	
}
