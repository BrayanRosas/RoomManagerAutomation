package ui.pages;

import entities.Resource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ui.BasePageObject;
import ui.BaseResourcePropertiesPage;

/**
 * Created with IntelliJ IDEA.
 * User: BrayanRosas
 * Date: 12/8/15
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddResourcePage extends BaseResourcePropertiesPage {

    private boolean isMessageShowed;

    @FindBy(xpath= "//form/div/input[@ng-model='resource.name']")
    private WebElement inputResourceName;

    @FindBy(xpath= "//form/div/input[@ng-model='resource.customName']")
    private WebElement inputResourceCustomName;

    @FindBy(xpath= "//div[@class='modal-footer ng-scope']/div/button[@class='info']")
    private WebElement buttonSaveResource;

    @FindBy(xpath= "//div[@class='modal-footer ng-scope']/div/button[@class='btn-clear']")
    private WebElement buttonCancelResource;

    @FindBy(xpath= "//small[@class='inline-error ng-binding']")
    private WebElement errorCreateResource;

    public AddResourcePage() {
        PageFactory.initElements(driver, this);
        waitUntilPageObjectIsLoaded();
    }

    @Override
    public void waitUntilPageObjectIsLoaded() {
        wait.until(ExpectedConditions.visibilityOf(buttonSaveResource));
    }

    public void  createResource(Resource resource1){
        inputResourceName.sendKeys(resource1.getName());
        inputResourceCustomName.sendKeys(resource1.getDisplayName());
        buttonSaveResource.click();
    }

    public boolean isMessageShowed(String message){
        if(errorCreateResource.getText().equalsIgnoreCase(message)){
            isMessageShowed=true;
        }

        else{
            isMessageShowed=false ;
        }

        return isMessageShowed;
    }

    public ResourcePage clickCancelResourceButton(){
        buttonCancelResource.click();
        return new ResourcePage();
    }
}
