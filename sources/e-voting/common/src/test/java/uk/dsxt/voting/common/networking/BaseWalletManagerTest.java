package uk.dsxt.voting.common.networking;

import org.junit.Ignore;
import org.junit.Test;
import uk.dsxt.voting.common.messaging.WalletManager;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class BaseWalletManagerTest {

    @Test
    @Ignore
    public void testNxtApi() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("nxt.jar.path", "../libs/nxt.jar");
        properties.setProperty("nxt.properties.path", "../conf/nxt-default.properties");
        properties.setProperty("nxt.peerServerPort", "7873");
        properties.setProperty("nxt.apiServerPort", "7872");
        properties.setProperty("nxt.dbDir", "./nxt-db-1");
        properties.setProperty("nxt.testDbDir", "./nxt-db-1");
        properties.setProperty("nxt.defaultTestnetPeers", "127.0.0.1:7973");
        properties.setProperty("nxt.defaultPeers", "127.0.0.1:7973");
        properties.setProperty("nxt.main.address", "NXT-9PHW-CVXU-2TDY-H4878");
        properties.setProperty("nxt.account.passphrase", "master_password");
        properties.setProperty("nxt.isOffline", "true");
        properties.setProperty("nxt.isTestnet", "true");
        properties.setProperty("nxt.timeMultiplier", "1000");
        properties.setProperty("nxt.minNeedBlocks", "1");
        BaseWalletManager wm1;
/*
        wm1 = new BaseWalletManager(properties, null, "1");
        wm1.runWallet();
        Thread.sleep(1000);
        assertEquals(wm1.getBalance().compareTo(new BigDecimal(1000000000L)), 0);
        Thread.sleep(60000);
        wm1.stopWallet();
        Thread.sleep(1000);
        */
        properties.setProperty("nxt.isOffline", "false");
        properties.setProperty("nxt.isTestnet", "true");
        properties.setProperty("nxt.timeMultiplier", "10");
        wm1 = new BaseWalletManager(properties, null, "11");
        wm1.runWallet();
        Thread.sleep(3000);
        properties.setProperty("nxt.peerServerPort", "7973");
        properties.setProperty("nxt.apiServerPort", "7972");
        properties.setProperty("nxt.dbDir", "./nxt-db-2");
        properties.setProperty("nxt.account.passphrase", "client_password");
        properties.setProperty("nxt.register.password", "master_password");
        properties.setProperty("nxt.defaultTestnetPeers", "127.0.0.1:7873;");
        properties.setProperty("nxt.defaultPeers", "127.0.0.1:7873;");
        WalletManager wm2 = new BaseWalletManager(properties, null, "2");
        wm2.runWallet();
        Thread.sleep(1000);
        String messageId = null;
        while (messageId == null) {
            messageId = wm2.sendMessage("test".getBytes(StandardCharsets.UTF_8));
            Thread.sleep(5000);
        }
        assertNotNull(messageId);
        System.out.println(messageId);
        List<Message> newMessages = null;
        while (newMessages == null || newMessages.size() == 0) {
            newMessages = wm1.getNewMessages(0);
            Thread.sleep(1000);
        }
        System.out.println(newMessages);
    }
}
