package org.list.app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.list.collections.MyList;
import org.list.types.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Controller {

    private MyList list;

    private UserType currentUserType;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextArea textArea;

    @FXML
    private Pane mainPane;

    @FXML
    public void initialize() throws IOException {
        list = new MyList();
        typeComboBox.getItems().addAll(
                List.of("MyFloat", "MyInteger", "MyString", "MyType")
        );
        typeComboBox.getSelectionModel().select(0);
        typeComboBox.fireEvent(new ActionEvent());
        updateText();
    }

    @FXML
    public void onSwitchTypeClicked(ActionEvent actionEvent) {
        currentUserType = switch (typeComboBox.getValue()) {
            case "MyFloat" -> MyFloat.createStatic();
            case "MyInteger" -> MyInteger.createStatic();
            case "MyString" -> MyString.createStatic();
            case "MyType" -> MyType.createStatic();
            default -> null;
        };
        list = new MyList();
        list.add(currentUserType);
        updateText();
    }

    @FXML
    public void onAddValClicked(ActionEvent actionEvent) {
        Object value = dialogValue();
        if (value != null) {
            list.add((UserType)value);
            updateText();
        }
    }

    @FXML
    public void onAddIndexClicked(ActionEvent actionEvent) {
        Pair<Integer, Object> input = dialogIndexValue();
        if (input != null && input.getKey() >= 0 && input.getKey() <= list.getSize()) {
            list.add((UserType) input.getValue(), input.getKey());
            updateText();
        }
    }

    @FXML
    public void onAddChildClicked(ActionEvent actionEvent) {
        Pair<Integer, Pair<Integer, Object>> input = dialogIndex2Value();
        boolean isFirstKeyValid = input != null &&
                                  input.getKey() >= 0 &&
                                  input.getKey() < list.getSize();
        boolean isValid = isFirstKeyValid &&
                          input.getValue().getKey() >= 0 &&
                          input.getValue().getKey() <= list.getChildSize(input.getKey());
        if (isValid) {
            list.addChild((UserType) input.getValue().getValue(), input.getKey(), input.getValue().getKey());
            updateText();
        }
    }

    @FXML
    public void onGetClicked(ActionEvent actionEvent) {
        Integer index = dialogIndex();
        if (index != null && index >= 0 && index <= list.getSize() - 1) {
            popUp("Get parent", String.format("Value of %d element = %s", index, list.getNode(index).getData()));
        }
    }

    @FXML
    public void onRemoveParentClicked(ActionEvent actionEvent) {
        Integer index = dialogIndex();
        if (index != null && index >= 0 && index <= list.getSize() - 1) {
            popUp("Remove parent", String.format("Removed element: %s", list.remove(index)));
            updateText();
        }
    }

    @FXML
    public void onRemoveChildClicked(ActionEvent actionEvent) {
        Pair<Integer, Integer> input = dialogIndex2();
        boolean isFirstIndex = input != null &&
                               input.getKey() >= 0 &&
                               input.getKey() <= list.getSize()-1;
        boolean isValid = isFirstIndex &&
                          input.getValue() >= 0 &&
                          input.getValue() <= list.getChildSize(input.getKey())-1;
        if (isValid) {
            popUp("Remove child", String.format(
                    "Removed element: %s",
                    list.removeChild(input.getKey(), input.getValue()))
            );
            updateText();
        }
    }

    @FXML
    public void onSizeClicked(ActionEvent actionEvent) {
        popUp("Size", String.format(
                "Size of parent list: %s\nTotal child elements: %s\nTotal elements: %s",
                list.getSize(), list.getTotalChildElements(), list.getSize() + list.getTotalChildElements())
        );
    }

    @FXML
    public void onCountChildClicked(ActionEvent actionEvent) {
        Integer index = dialogIndex();
        if (index != null && index >= 0 && index <= list.getSize() - 1) {
            popUp("Size", String.format("Size of childs list: %s", list.getNode(index).getChilds()));
        }
    }

    @FXML
    public void onSortParentClicked(ActionEvent actionEvent) {
        if (list.getSize() > 0) {
            list.quickSort(0, list.getSize() - 1, currentUserType.getTypeComparator());
            updateText();
        }
    }

    @FXML
    public void onSortChildClicked(ActionEvent actionEvent) {
        if (list.getTotalChildElements() > 0) {
            for (int i = 0; i < list.getSize(); i++) {
                list.quickSortChild(i, 0, list.getChildSize(i) - 1, currentUserType.getTypeComparator());
            }
            updateText();
        }
    }

    @FXML
    public void onRandomClicked(ActionEvent actionEvent) {
        Random r = new Random();
        Integer randChildNum = r.nextInt(5);
        Object value = currentUserType.create();
        list.add((UserType) value);
        for (int i=0; i < randChildNum; ++i) {
            list.addChild((UserType) currentUserType.create(), list.getSize()-1);
        }
        updateText();
    }

    @FXML
    public void onRandomNClicked(ActionEvent actionEvent) {
        Random r = new Random();
        for (int i = 0; i < 1000; ++i) {
            Integer randChildNum = r.nextInt(5);
            Object value = currentUserType.create();
            list.add((UserType) value);
            for (int j=0; j < randChildNum; ++j) {
                list.addChild((UserType) currentUserType.create(), list.getSize()-1);
            }
        }
        updateText();
    }

    @FXML
    public void onFromBinClicked(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        list = MyList.deserializeFromBinary();
    }

    @FXML
    public void onToBinClicked(ActionEvent actionEvent) throws IOException {
        if (list.getSize() > 0) {
            MyList.serializeToBinary(list);
        }
    }

    private Pair<Integer, Pair<Integer, Object>> dialogIndex2Value() {
        Dialog<Pair<Integer, Pair<Integer, Object>>> dialog = new Dialog<>();
        dialog.setTitle("Input Reader");
        dialog.setHeaderText("Please enter index1, index2 and value:");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 20));

        TextField key1 = new TextField();
        key1.setPromptText("Index1");
        TextField key2 = new TextField();
        key2.setPromptText("Index2");
        TextField value = new TextField();
        value.setPromptText("Value");

        gridPane.add(key1, 0, 0);
        gridPane.add(key2, 0, 1);
        gridPane.add(value, 0, 2);

        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(key1::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                Integer dkey1 = 0;
                Integer dkey2 = 0;
                Object val = null;
                try {
                    dkey1 = Integer.parseInt(key1.getText());
                    dkey2 = Integer.parseInt(key2.getText());
                    val = currentUserType.parseValue(value.getText());
                    return new Pair<>(dkey1, new Pair<>(dkey2, val));
                } catch (NumberFormatException |
                         ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Invalid input format: key1 = " + key1.getText() + ", key2 = " + key2.getText()
                            + " val = " + value.getText());
                }
            }
            return null;
        });

        Optional<Pair<Integer, Pair<Integer, Object>>> pair = dialog.showAndWait();

        if (pair.isPresent()) {
            return pair.get();
        } else {
            return null;
        }
    }

    private Pair<Integer, Object> dialogIndexValue() {
        Dialog<Pair<Integer, Object>> dialog = new Dialog<>();
        dialog.setTitle("Input Reader");
        dialog.setHeaderText("Please enter index and value:");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 20));

        TextField key = new TextField();
        key.setPromptText("Index");
        TextField value = new TextField();
        value.setPromptText("Value");

        gridPane.add(key, 0, 0);
        gridPane.add(value, 0, 1);

        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(key::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                Integer dkey = 0;
                Object val = null;
                try {
                    dkey = Integer.parseInt(key.getText());
                    val = currentUserType.parseValue(value.getText());
                    return new Pair<>(dkey, val);
                } catch (NumberFormatException |
                         ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Invalid input format: key = " + key.getText() + ", val = " + value.getText());
                }
            }
            return null;
        });

        Optional<Pair<Integer, Object>> pair = dialog.showAndWait();

        if (pair.isPresent()) {
            return pair.get();
        } else {
            return null;
        }
    }

    private Pair<Integer, Integer> dialogIndex2() {
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Input Reader");
        dialog.setHeaderText("Please enter index1 and index2:");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 20));

        TextField key = new TextField();
        key.setPromptText("Index1");
        TextField value = new TextField();
        value.setPromptText("Index2");

        gridPane.add(key, 0, 0);
        gridPane.add(value, 0, 1);

        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(key::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                Integer dkey1 = 0;
                Integer dkey2 = 0;
                try {
                    dkey1 = Integer.parseInt(key.getText());
                    dkey2 = Integer.parseInt(value.getText());
                    return new Pair<>(dkey1, dkey2);
                } catch (NumberFormatException |
                         ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Invalid input format: key = " + key.getText() + ", val = " + value.getText());
                }
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> pair = dialog.showAndWait();

        if (pair.isPresent()) {
            return pair.get();
        } else {
            return null;
        }
    }

    private Integer dialogIndex() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Input Reader");
        dialog.setHeaderText("Please enter index:");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 20));

        TextField key = new TextField();
        key.setPromptText("Key");
        gridPane.add(key, 0, 0);

        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(key::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                Integer dkey = 0;
                try {
                    dkey = Integer.parseInt(key.getText());
                    return dkey;
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input format: " + key.getText());
                }
            }
            return null;
        });

        Optional<Integer> res = dialog.showAndWait();

        if (res.isPresent()) {
            return res.get();
        } else {
            return null;
        }
    }

    private Object dialogValue() {
        Dialog<Object> dialog = new Dialog<>();
        dialog.setTitle("Input Reader");
        dialog.setHeaderText("Please enter value:");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 20));

        TextField value = new TextField();
        value.setPromptText("Value");
        gridPane.add(value, 0, 0);

        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(value::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                Object dval;
                try {
                    dval = currentUserType.parseValue(value.getText());
                    return dval;
                } catch (NumberFormatException |
                         ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Invalid input format: val = " + value.getText());
                }
            }
            return null;
        });

        Optional<Object> res = dialog.showAndWait();

        if (res.isPresent()) {
            return res.get();
        } else {
            return null;
        }
    }

    private void popUp(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void updateText() {
        textArea.setText(list.toString());
    }
}
