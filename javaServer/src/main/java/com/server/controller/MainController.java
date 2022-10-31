package com.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGasPrice;
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
import java.util.Map;

@Controller
public class MainController {
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @RequestMapping(value = "/")
    public String index(Model model) throws Exception {
        Web3j web3 = Web3j.build(new HttpService("http://localhost:8545"));
        System.out.println("connected");
        System.out.println(web3);

        // Gas Price
        EthGasPrice gasPrice = web3.ethGasPrice().send();
        System.out.println(gasPrice.getGasPrice());
        
        // 계좌 전부 가져오기
        EthAccounts accounts = web3.ethAccounts().send();
        System.out.println( accounts.getAccounts());

        // List에 담기
        List<Account> accountList = new ArrayList<>();
        for (String account: accounts.getAccounts()) {
            BigDecimal getBalance = Convert.fromWei(web3.ethGetBalance(account, DefaultBlockParameterName.LATEST).send().getBalance().toString(), Convert.Unit.ETHER);
            Account myAccount = new Account(account, getBalance);
            accountList.add(myAccount);
        }
        System.out.println("account: "+accountList);

        // Json 변경
        ObjectMapper mapper = new ObjectMapper();
        String accountListJson = mapper.writeValueAsString(accountList);
        System.out.println(accountListJson);

        // 보내기
//        String host_url = "https://webhook.site/0dab137c-fafe-43de-95ad-3a076554d09f";
//        String host_url = "http://localhost:3000/data";
//        HttpURLConnection conn = null;
//
//        URL url = new URL(host_url);
//        conn = (HttpURLConnection)url.openConnection();
//        conn.setConnectTimeout(10000);
//
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type","application/json");
//
//        conn.setDoOutput(true);
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
//
//        bw.write(accountListJson);
//        bw.flush();
//        bw.close();
//
//        // 서버에서 보낸 응답 데이터 수신 받기
//        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String returnMsg = in.readLine();
//        System.out.println("응답메시지: "+returnMsg);
//
//        // HTTP 응답 코드 수신
//        int responseCode = conn.getResponseCode();
//        if(responseCode == 400) {
//            System.out.println("400: 명령을 실행 오류");
//        } else if(responseCode == 500) {
//            System.out.println("500: 서버 에러");
//        } else {
//            System.out.println(responseCode + ": 응답코드임");
//        }
//
//        model.addAttribute("data",accountListJson);

        return accountListJson;
    }

    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
    @RequestMapping(value="/data", method = RequestMethod.POST)
    public ResponseEntity Data(@RequestBody ArrayList<Map<Object, Object>> data) {
        System.out.println("data: "+data);
        return new ResponseEntity(data,HttpStatus.OK);
    }
}
