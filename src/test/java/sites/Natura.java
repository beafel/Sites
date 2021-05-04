// Pacote
package sites;

//Bibliotecas

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Classes
public class Natura {

    //Atributos
    WebDriver driver;
    String url;
    WebDriverWait wait;
    String pastaPrint = "evidencias/" + new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime()) + "/";

    //Metodos e Funcoes - Tirar Prints
    public void tirarPrint(String nomePrint) throws IOException {

        //variavel foto/ especializar Selenium para tirar foto/ gravar em arquivo/ nome do arquivo
        File foto = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(foto,new File(pastaPrint + nomePrint + ".png"));
    }

    @Before
    public void iniciar(){
        url = "https://natura.com.br";
        System.setProperty("webdriver.edge.driver","drivers/edge/msedgedriver90.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);
        //driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
    }

    @After
    public void finalizar(){
        driver.quit();
    }

    @Test
    public void consultaProdutoNatura() throws IOException {
        driver.get(url);

        //Aguardar carregar o site
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("Busca")));


        tirarPrint("Print 1 - Exibe Site da Natura");
        driver.findElement(By.cssSelector("span.SearchPreview_searchLabel__rS3z8")).click();
        driver.findElement(By.cssSelector("span.SearchPreview_searchLabel__rS3z8")).sendKeys("KAIAK MASCULINO" + Keys.ENTER);
        tirarPrint("Print 2 - Exibe pesquisa pelo produto KAIAK MASCULINO");

        //Validar produtos encontrados na pesquisa
        assertEquals("KAIAK MASCULINO", driver.findElement(By.cssSelector("span.SearchTitle_search-title__main__3c857")).getText());

        //fechar pop-up
        //driver.findElement(By.cssSelector("a.btClose")).click();

        driver.findElement(By.tagName("Kaiak Masculino")).click();
        tirarPrint("Passo 3 - Exibe a pagina do produto KAIAK MASCULINO");

        //Validar valor e parcelamento do produto
        assertEquals("R$ 121,90", driver.findElement(By.cssSelector("span.price-value")).getText());
        assertTrue(driver.findElement(By.cssSelector("InstallmentPhrase_phrase__1wrE8")).getText().contains("4x de R$ 30,48 sem juros"));
    }
}
