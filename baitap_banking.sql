-- deposit --
delimiter //
CREATE PROCEDURE deposit(
    IN customer_id INT,
    IN amount DECIMAL(10,2)
)
BEGIN
    -- Tìm thông tin tài khoản
		SELECT count(*)
		FROM customers c
		WHERE id = customer_id
		INTO @count_customer;
    
    if (@count_customer = 0) then 
			SET @MESSAGE = 'Customer not found.';
			select @MESSAGE;
        else 
            INSERT INTO `deposits`(created_at, customer_id, transaction_amount)  values
			(now(), customer_id, amount);
            -- Cập nhật số dư tài khoản
			SET @new_balance = (select balance from customers where id =  customer_id) + amount;

			UPDATE customers
			SET balance = @new_balance
			WHERE id = customer_id;
			
			-- Trả về thông tin tài khoản sau khi gửi tiền
			SELECT full_name, balance
			FROM customers
			WHERE id = customer_id;
	end if;

END //
delimiter ;

-- withdraw --
delimiter //
drop procedure if exists `withdraw`;
CREATE PROCEDURE withdraw(
    IN customer_id INT,
    IN amount DECIMAL(10,2)
)
BEGIN
	DECLARE has_error INT DEFAULT 0;
	DECLARE error_message VARCHAR(255);
    
    -- Tìm thông tin tài khoản
		SELECT count(*) FROM customers c
		WHERE id = customer_id
		INTO @count_customer;
    
    if (@count_customer = 0) then 
			SET has_error = 1;
			SET error_message = 'Khong tim thay khach hang';
        else 
        -- Tính số tiền hiện tại của khách hàng
			set @current_balance = (select balance from customers where id =  customer_id);
            if (@current_balance >= amount) then
					INSERT INTO `withdraws`(created_at, customer_id, transaction_amount)  values
					(now(), customer_id, amount);
					-- Cập nhật số dư tài khoản
					SET @new_balance = @current_balance - amount;

					UPDATE customers
					SET balance = @new_balance
					WHERE id = customer_id;
			else
					SET has_error = 1;
                    SET error_message = CONCAT('So tien muon rut khong hop le');	
			end if;
	end if;
	
    if (has_error = 1) then 
		select has_error as errors, error_message as `message`;
	else 
    			-- Trả về thông tin tài khoản sau khi gửi tiền
			SELECT full_name, balance
			FROM customers
			WHERE id = customer_id;
	end if;
END //
delimiter ;

-- transfer --
delimiter //
drop procedure if exists `transfer`;
CREATE PROCEDURE transfer(
    IN sender_id INT,
    IN recipient_id INT,
    IN amount DECIMAL(10,2)
)
BEGIN
	DECLARE has_error INT DEFAULT 0;
	DECLARE error_message VARCHAR(255);
    
    -- Tìm thông tin tài khoản gửi
		SELECT count(*) FROM customers c
		WHERE id = sender_id
		INTO @count_sender;
        
	-- Tìm thông tin tài khoản nhận
		SELECT count(*) FROM customers c
		WHERE id = recipient_id
		INTO @count_recipient;
        
    if ((@count_sender = 0) or (@count_recipient = 0)) then 
			SET has_error = 1;
			SET error_message = 'Khong tim thay nguoi gui hoac nguoi nhan';
        else 
        -- Tính số tiền hiện tại của người gửi
			set @current_balance_sender = (select balance from customers where id = sender_id);
			set @current_balance_recipient = (select balance from customers where id = recipient_id);
            
            if (@current_balance_sender >= (amount + 0.1 * amount)) then
					-- Thêm vào bảng transfer
                    INSERT INTO `transfers`(created_at, fees, fees_amount, transaction_amount, transfer_amount, `sender_id`, `recipient_id`)  
                    values(now(), 10, 0.1 * amount, amount, amount - (0.1 * amount), sender_id, recipient_id);
					-- Cập nhật số dư tài khoản người gửi
					SET @new_balance_sender = @current_balance_sender - (amount + 0.1 * amount);
					SET @new_balance_recipient = @current_balance_recipient + amount;
					
					UPDATE customers
					SET balance = @new_balance_sender
					WHERE id = sender_id;
                    -- Cập nhật số dư tài khoản người nhận
                    UPDATE customers
					SET balance = @new_balance_recipient
					WHERE id = recipient_id;
			else
					SET has_error = 1;
                    SET error_message = CONCAT('So tien muon chuyen khong hop le');	
			end if;
	end if;
	
    if (has_error = 1) then 
		select has_error as errors, error_message as `message`;
	else 
    			-- Trả về thông tin tài khoản sau khi chuyển tiền
				SELECT * FROM banking.customers;
	end if;
END //
delimiter ;


-- view --
create view transferHistory as
select sender_id, recipient_id, fees, fees_amount, transfer_amount, created_at 
from `transfers`;
select * from transferHistory;