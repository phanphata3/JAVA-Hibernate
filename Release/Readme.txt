########################## Read Me ############################
Name 		:	Phan Tấn Phát
MSSV 		:	1312423
Mail		: 	ptphat.hcmus@gmail.com
Software	:	Eclipse

########################## Guide to Run #######################

- Vì file nén khá nặng nên thư mục Source không chứa thư viện. Do vậy, cần copy thư viện (tất cả file .jar)
 từ Release\1312423_lib đến thư mục Source\1312423_3\libs.

- Thông tin đăng nhập mặc định với MySQL là "root", "1234" cần thay đổi trong file "hibernate.cfg.xml" cho phù hợp để connect đến database.

- File 1312423.jar cần đặt cùng cấp với thư viện 1312423_lib để thực thi

Cách chạy chương trình (1 trong 2 cách):
	+ Double click file 1312423.jar
	+ Sử dụng command line "java -jar 1312423.jar"