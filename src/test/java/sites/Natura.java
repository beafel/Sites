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
        System.setProperty("webdriver.edge.driver","drivers/edge/msedgedriver101.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);
        driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
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
        //fechar pop-up
        //driver.findElement(By.cssSelector("a.btClose")).click();

        driver.findElement(By.name("search")).click();
        driver.findElement(By.name("search")).sendKeys("Kaiak Masculino" + Keys.ENTER);

        tirarPrint("Print 1 - Exibe Site da Natura");

        //Validar produtos encontrados na pesquisa
        assertTrue(driver.findElement(By.cssSelector("div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-12")).getText().contains("KAIAK MASCULINO"));

        //tirarPrint("Print 2 - Exibe pesquisa pelo produto KAIAK MASCULINO");

        driver.findElement(By.xpath("//h5[contains(text(),'Kaiak Desodorante Colônia Masculino')]")).click();

        //Validar valor e parcelamento do produto
        assertEquals("R$ 139,90", driver.findElement(By.cssSelector("p.MuiTypography-root.natds336.MuiTypography-body1")).getText());
        assertTrue(driver.findElement(By.cssSelector("p.MuiTypography-root.natds342.natds692.MuiTypography-body1")).getText().contains("4x de R$ 34,98 sem juros"));

        //tirarPrint("Print 3 - Exibe a pagina do produto KAIAK MASCULINO");
    }
}
