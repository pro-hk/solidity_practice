package abcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
	 public static void main(String[] args) throws Exception {
	        // ����
	        Web3j web3 = Web3j.build(new HttpService("http://localhost:8545"));
	        System.out.println("connected");

	        // ����
	        Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
	        System.out.println("Client Version: "+clientVersion.getWeb3ClientVersion());

	        // ����
	        EthGasPrice gasPrice = web3.ethGasPrice().send();
	        System.out.println("Default Gas Price: "+gasPrice.getGasPrice());

	        // �ܾ�
	        EthGetBalance ethGetBalance = web3.ethGetBalance("0x67f1987B7f82eb0DA32C660feEdcF1FC64406c37", DefaultBlockParameterName.LATEST).sendAsync().get();
	        System.out.println("balance of Account '0x67f1987B7f82eb0DA32C660feEdcF1FC64406c37' "+ethGetBalance.getBalance());

	        // �ܾ� Ether ��ȯ
	        System.out.println("Balance in Ether format: " + Convert.fromWei(web3.ethGetBalance("0x67f1987B7f82eb0DA32C660feEdcF1FC64406c37", DefaultBlockParameterName.LATEST).send().getBalance().toString(), Convert.Unit.ETHER)+ "ETH");

	        //���� ���� ���� ��������
	        EthAccounts accounts = web3.ethAccounts().send();
	        System.out.println( accounts.getAccounts());

	        //
	        List<Account> accountList = new ArrayList<>();
	        for (String account: accounts.getAccounts()) {
	            BigDecimal getBalance = Convert.fromWei(web3.ethGetBalance(account, DefaultBlockParameterName.LATEST).send().getBalance().toString(), Convert.Unit.ETHER);
	            Account myAccount = new Account(account, getBalance);
	            accountList.add(myAccount);
	        }
	        System.out.println("account: "+accountList);

	        ObjectMapper mapper = new ObjectMapper();
	        String accountListJson = mapper.writeValueAsString(accountList);
	        System.out.println(accountListJson);

	        // ����
//	        new ServerSocket(3001);

	        // ������
//	        String host_url = "https://webhook.site/0dab137c-fafe-43de-95ad-3a076554d09f";
	        String host_url = "http://localhost:3000/data";
	        HttpURLConnection conn = null;

	        URL url = new URL(host_url);
	        conn = (HttpURLConnection)url.openConnection();

	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type","application/json");

	        conn.setDoOutput(true);
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

	        bw.write(accountListJson);
	        bw.flush();
	        bw.close();

	        // �������� ���� ���� ������ ���� �ޱ�
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String returnMsg = in.readLine();
	        System.out.println("����޽���: "+returnMsg);

	        // HTTP ���� �ڵ� ����
	        int responseCode = conn.getResponseCode();
	        if(responseCode == 400) {
	            System.out.println("400: ������ ���� ����");
	        } else if(responseCode == 500) {
	            System.out.println("500: ���� ����");
	        } else {
	            System.out.println(responseCode + ": �����ڵ���");
	        }
	 }
}