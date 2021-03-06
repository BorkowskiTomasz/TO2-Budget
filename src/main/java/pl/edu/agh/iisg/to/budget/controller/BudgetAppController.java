package pl.edu.agh.iisg.to.budget.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.iisg.to.budget.Main;
import pl.edu.agh.iisg.to.budget.controller.generators.DataGenerator;
import pl.edu.agh.iisg.to.budget.model.Budget;
import pl.edu.agh.iisg.to.budget.model.Category;

import java.io.IOException;
import java.util.*;

/**
 * Created by tom on 24.11.15.
 */
public class BudgetAppController {
    private static final Logger logger = LogManager.getLogger(BudgetAppController.class);

    private Stage stage;
    BudgetManager budgetManager;

    Random random = new Random();
    public BudgetAppController(Stage stage) {
        this.stage = stage;
    }

    public void initRootLayout() {
        try {
            this.stage.setTitle("Budget");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BudgetOverviewPane.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            BudgetOverviewController controller = loader.getController();

            budgetManager = new BudgetManager();

            DataGenerator dataGenerator = new DataGenerator(budgetManager);
            List<Budget> data = dataGenerator.getData();
            budgetManager.setData(data);
            controller.setAppController(this);
            controller.setBudgetManager(budgetManager);
            controller.setData(budgetManager.getParentCategories(), budgetManager.getData());
            budgetManager.updateGeneralBudget();

            controller.setGeneralPla(budgetManager.getGeneralBudget());
            controller.setAppController(this);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    public boolean showBudgetEditDialog(Budget budget) {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BudgetEditDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit budget");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BudgetEditDialogController controller = loader.getController();
            controller.setAppController(this);
            controller.setBudgetManager(budgetManager);
            controller.setDialogStage(dialogStage);
            controller.setCategories(budgetManager.getSubcategories());
            controller.setParentCategories(budgetManager.getParentCategories());
            controller.setData(budget);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showAddCategoryDialog(Category category) {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AddCategoryDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Category");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddCategoryDialogController controller = loader.getController();
            controller.setAppController(this);
            controller.setBudgetManager(budgetManager);
            controller.setDialogStage(dialogStage);
            controller.setCategory(category);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
