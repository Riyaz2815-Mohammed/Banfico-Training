-- Clears all transactions and payments, then seeds realistic transaction history
-- for every account that exists in the database.
-- Running balance (balance_after) is recalculated correctly for every row.
-- Run once against your PostgreSQL database:
--   psql $DATABASE_URL -f seed_transactions.sql

DO $$
DECLARE
    acc         RECORD;
    running     INTEGER;
    tx_time     TIMESTAMP;
    amt         INTEGER;
    bal_after   INTEGER;
BEGIN
    -- wipe dependent tables first
    DELETE FROM payments;
    DELETE FROM transactions;

    FOR acc IN SELECT account_id, account_type FROM accounts LOOP

        -- every account starts from a clean 50,000 opening balance
        running  := 50000;
        tx_time  := NOW() - INTERVAL '6 months';

        UPDATE accounts SET balance = running WHERE account_id = acc.account_id;

        -- 1. Account opened / initial deposit
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'CREDIT', 50000, tx_time, running, 'Account Opened - Initial Deposit', acc.account_id);

        -- 2. Salary credit (month -5)
        tx_time := tx_time + INTERVAL '3 days';
        amt     := CASE acc.account_type WHEN 'SAVINGS' THEN 45000 WHEN 'CURRENT' THEN 120000 ELSE 30000 END;
        running := running + amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'CREDIT', amt, tx_time, running, 'Salary Credit - NEFT', acc.account_id);

        -- 3. ATM withdrawal
        tx_time := tx_time + INTERVAL '2 days';
        amt     := 5000;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'ATM Cash Withdrawal', acc.account_id);

        -- 4. Electricity bill
        tx_time := tx_time + INTERVAL '4 days';
        amt     := 1850;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Electricity Bill - BESCOM', acc.account_id);

        -- 5. UPI purchase
        tx_time := tx_time + INTERVAL '3 days';
        amt     := 2340;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'UPI - Swiggy Food Order', acc.account_id);

        -- 6. Salary credit (month -4)
        tx_time := tx_time + INTERVAL '18 days';
        amt     := CASE acc.account_type WHEN 'SAVINGS' THEN 45000 WHEN 'CURRENT' THEN 120000 ELSE 30000 END;
        running := running + amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'CREDIT', amt, tx_time, running, 'Salary Credit - NEFT', acc.account_id);

        -- 7. Mobile recharge
        tx_time := tx_time + INTERVAL '2 days';
        amt     := 599;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Jio Recharge - UPI', acc.account_id);

        -- 8. Loan EMI
        tx_time := tx_time + INTERVAL '5 days';
        amt     := 12500;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Home Loan EMI - HDFC', acc.account_id);

        -- 9. Online shopping
        tx_time := tx_time + INTERVAL '6 days';
        amt     := 3799;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Amazon Purchase - Debit Card', acc.account_id);

        -- 10. Refund received
        tx_time := tx_time + INTERVAL '3 days';
        amt     := 3799;
        running := running + amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'CREDIT', amt, tx_time, running, 'Amazon Refund Received', acc.account_id);

        -- 11. Salary credit (month -3)
        tx_time := tx_time + INTERVAL '12 days';
        amt     := CASE acc.account_type WHEN 'SAVINGS' THEN 45000 WHEN 'CURRENT' THEN 120000 ELSE 30000 END;
        running := running + amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'CREDIT', amt, tx_time, running, 'Salary Credit - NEFT', acc.account_id);

        -- 12. Grocery
        tx_time := tx_time + INTERVAL '4 days';
        amt     := 4200;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Grocery - BigBasket UPI', acc.account_id);

        -- 13. Internet bill
        tx_time := tx_time + INTERVAL '7 days';
        amt     := 999;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Internet Bill - Airtel', acc.account_id);

        -- 14. ATM withdrawal
        tx_time := tx_time + INTERVAL '5 days';
        amt     := 8000;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'ATM Cash Withdrawal', acc.account_id);

        -- 15. Salary credit (month -2)
        tx_time := tx_time + INTERVAL '7 days';
        amt     := CASE acc.account_type WHEN 'SAVINGS' THEN 45000 WHEN 'CURRENT' THEN 120000 ELSE 30000 END;
        running := running + amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'CREDIT', amt, tx_time, running, 'Salary Credit - NEFT', acc.account_id);

        -- 16. Loan EMI
        tx_time := tx_time + INTERVAL '1 day';
        amt     := 12500;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Home Loan EMI - HDFC', acc.account_id);

        -- 17. Electricity bill
        tx_time := tx_time + INTERVAL '8 days';
        amt     := 2100;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Electricity Bill - BESCOM', acc.account_id);

        -- 18. UPI transfer received
        tx_time := tx_time + INTERVAL '3 days';
        amt     := 5000;
        running := running + amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'CREDIT', amt, tx_time, running, 'UPI Credit - Friend', acc.account_id);

        -- 19. Salary credit (month -1)
        tx_time := tx_time + INTERVAL '12 days';
        amt     := CASE acc.account_type WHEN 'SAVINGS' THEN 45000 WHEN 'CURRENT' THEN 120000 ELSE 30000 END;
        running := running + amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'CREDIT', amt, tx_time, running, 'Salary Credit - NEFT', acc.account_id);

        -- 20. Loan EMI
        tx_time := tx_time + INTERVAL '1 day';
        amt     := 12500;
        running := running - amt;
        INSERT INTO transactions (transaction_id, transaction_type, amount, transaction_time, balance_after, description, account_id)
        VALUES (gen_random_uuid(), 'DEBIT', amt, tx_time, running, 'Home Loan EMI - HDFC', acc.account_id);

        -- sync final account balance
        UPDATE accounts SET balance = running WHERE account_id = acc.account_id;

    END LOOP;
END $$;
