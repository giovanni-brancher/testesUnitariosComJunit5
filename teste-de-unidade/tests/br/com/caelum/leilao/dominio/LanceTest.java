package br.com.caelum.leilao.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class LanceTest {
	
	Usuario pedro = new Usuario("Pedro");

	@Test
	public void naoDeveAceitarLanceComValorZero() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Lance(pedro, 0.00);
	    });
	}
	
	@Test
	public void naoDeveAceitarLanceComValorNegativo() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Lance(pedro, -1.00);
	    });
	}
	
	@Test
	public void deveAceitarLanceComValorPositivo() {
		assertEquals((new Lance(pedro, 1.00).getClass()), Lance.class);
	}
}
