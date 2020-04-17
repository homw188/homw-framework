package com.homw.transport.ssh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.ethz.ssh2.Connection;

public class TestSsh2Client {
	
	@Test
	public void testLogin() {
		Ssh2Client client = new Ssh2Client("192.168.100.20", "admin", "admin123");
		Connection conn = client.login();
		assertNotNull(conn);
	}
	
	@Test
	public void testExecCommand() {
		Ssh2Client client = new Ssh2Client("192.168.100.20", "admin", "admin123");
		String result = client.execCommand("whoami");
		assertEquals(result, "admin\n");
	}

}
