package br.com.pmattiollo;

import org.junit.Assert;
import org.junit.Test;

import br.com.pmattiollo.entidades.Usuario;

public class AssertTest {

	@Test
	public void teste() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);

		Assert.assertEquals("Erro de comparação", 1, 1);
		Assert.assertEquals(0.51234, 0.512, 0.001);
		Assert.assertEquals(Math.PI, 3.14, 0.01);

		int i1 = 5;
		Integer i2 = new Integer(5);
		Assert.assertEquals(i1, i2.intValue());
		Assert.assertEquals(Integer.valueOf(i1), i2);

		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "casa");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));

		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;
		Assert.assertEquals(u1, u2);
		Assert.assertSame(u1, u1);
		Assert.assertNotSame(u1, u2);
		Assert.assertNull(u3);
	}

}
