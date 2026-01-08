package com.yourname.financetracker.core;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import com.yourname.financetracker.model.Debt;
import com.yourname.financetracker.model.Expense;
import com.yourname.financetracker.model.Income;
import com.yourname.financetracker.service.FinanceManager;

public class FinanceXMLWriter {

    public static void save(FinanceManager manager) {
        try {
            Path filePath = FinancePaths.getDataFilePath();

            // Klasörü garanti altına al
            Files.createDirectories(filePath.getParent());

            DocumentBuilder builder =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("finance");
            doc.appendChild(root);

            // ========= INCOMES =========
            Element incomesEl = doc.createElement("incomes");
            root.appendChild(incomesEl);

            for (Income i : manager.getIncomes()) {
                Element incomeEl = doc.createElement("income");
                incomeEl.setAttribute("amount", String.valueOf(i.getAmount()));
                incomeEl.setAttribute("date", i.getDate().toString());

                Element desc = doc.createElement("description");
                desc.setTextContent(i.getDescription());

                incomeEl.appendChild(desc);
                incomesEl.appendChild(incomeEl);
            }

            // ========= EXPENSES =========
            Element expensesEl = doc.createElement("expenses");
            root.appendChild(expensesEl);

            for (Expense e : manager.getExpenses()) {
                Element expenseEl = doc.createElement("expense");
                expenseEl.setAttribute("amount", String.valueOf(e.getAmount()));
                expenseEl.setAttribute("date", e.getDate().toString());

                Element desc = doc.createElement("description");
                desc.setTextContent(e.getDescription());

                expenseEl.appendChild(desc);
                expensesEl.appendChild(expenseEl);
            }

            // ========= DEBTS =========
            Element debtsEl = doc.createElement("debts");
            root.appendChild(debtsEl);

            for (Debt d : manager.getAllDebts()) {
                Element debtEl = doc.createElement("debt");
                debtEl.setAttribute("amount", String.valueOf(d.getAmount()));
                debtEl.setAttribute("date", d.getDate().toString());
                debtEl.setAttribute("deadline", d.getDeadline().toString());
                debtEl.setAttribute("paid", String.valueOf(d.isPaid()));

                Element desc = doc.createElement("description");
                desc.setTextContent(d.getDescription());

                debtEl.appendChild(desc);
                debtsEl.appendChild(debtEl);
            }

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4"
            );

            transformer.transform(
                    new DOMSource(doc),
                    new StreamResult(new File(filePath.toString()))
            );

        } catch (Exception e) {
            throw new RuntimeException("XML kaydedilemedi", e);
        }
    }
    }