package com.xtb.demo;

import pro.xstore.api.message.codes.TRADE_OPERATION_CODE;
import pro.xstore.api.message.codes.TRADE_TRANSACTION_TYPE;
import pro.xstore.api.message.error.*;
import pro.xstore.api.message.records.SymbolRecord;
import pro.xstore.api.message.command.*;
import pro.xstore.api.message.records.TradeTransInfoRecord;
import pro.xstore.api.message.response.*;
import pro.xstore.api.sync.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.UnknownHostException;

@SpringBootApplication
public class XtbDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(XtbDemoApplication.class, args);

		try {
			SyncAPIConnector connector = new SyncAPIConnector(ServerData.ServerEnum.DEMO);
			Credentials credentials = new Credentials(####, ####);
			LoginResponse loginResponse = APICommandFactory.executeLoginCommand(
					connector,         // APIConnector
					credentials        // Credentials
			);
			if(loginResponse.getStatus() == true) {
				System.out.println("User logged in");
				SymbolResponse symbolResponse = APICommandFactory.executeSymbolCommand(connector, "EURUSD");
				double price = symbolResponse.getSymbol().getAsk();
				System.out.println(price);
				double sl = 0.0;
				double tp = 0.0;
				String symbol = symbolResponse.getSymbol().getSymbol();
				double volume = 0.1;
				long order = 0;
				String customComment = "my comment";
				long expiration = 0;
				TradeTransInfoRecord ttOpenInfoRecord = new TradeTransInfoRecord(
						TRADE_OPERATION_CODE.BUY,
						TRADE_TRANSACTION_TYPE.OPEN,
						price, sl, tp, symbol, volume, order, customComment, expiration);
				TradeTransactionResponse tradeTransactionResponse = APICommandFactory.executeTradeTransactionCommand(connector, ttOpenInfoRecord);

			} else {
				System.err.println("Error: user couldn't log in!");
			}

			connector.close();
			System.out.println("Connection closed");

			// Catch errors
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (APICommandConstructionException e) {
			e.printStackTrace();
		} catch (APICommunicationException e) {
			e.printStackTrace();
		} catch (APIReplyParseException e) {
			e.printStackTrace();
		} catch (APIErrorResponse e) {
			e.printStackTrace();
		}
	}
}

