This is dental appointments management system.

Admin can create the user with three different role such as "ADMIN", "MEMBER" and "DENTIST".
ADMIN role user can access all the endpoints.
MEMBER role user can create account, create appointments, search appointments by their patient number.
DENTIST role user can search their appointments with the detail info of patients and surgery.
Want to enhance more functionality such as billing, and status.

UML Diagram
<img width="1042" alt="image" src="https://github.com/user-attachments/assets/95936dd7-910a-401c-b30f-14e2fdb05021" />

Tech Stack
1. Java 21
2. Spring Boot
3. Spring Web
4. Spring Security
5. MySQL
6. H2 DB for testing
7. Docker
8. Azure

1. Create member account
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/0f942b1d-3d6f-421f-bb49-1586961d68bf" />
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/7e102b37-2286-4a2a-af5c-7af1410f5904" />

2. Create admin account
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/b1b9d714-c686-487a-b8cc-4eb89fb8f47d" />

3. Create dentist account
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/03e4d3c5-a477-47d6-a10e-3a3d7bf353d5" />

4. Users table
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/ea759df4-f3a5-4b4e-a2e4-21e917ada935" />

5. Create Patient with dentist role (403 Error)
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/2208feed-f4eb-46a4-ae59-043391fb8e15" />

6. Create Patient with member role
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/3d44edb6-795c-4e1e-80d2-ac6cb6cc01ca" />

   (Table)
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/7b9919de-8325-4c1f-9e5b-c3281b91a153" />

8. Create Patient with admin role
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/9471202f-2925-4c10-b424-02f2ed825203" />

   (Table)
   <img width="1440" alt="image" src="https://github.com/user-attachments/assets/e14120c5-d1f4-4468-bc99-02c396cb58a0" />

10. Patient already existed
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/a0471fb9-041b-440a-9231-a8d8b3e57eb2" />

11. Date format error
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/3f5bc82b-1993-471a-9a27-5996caba3fbb" />
   
13. Create Dentist with dentist role (403)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/2e14cf15-31cb-4f40-9cc5-17514d62fcaa" />
    
15. Create Dentist with member role (403)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/f68a9493-b8d8-49ea-8acd-6809004ef47f" />
    
17. Create Dentist with admin role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/e64ac8a3-1a2f-4def-961c-a37533b0544f" />

    (Table)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/970824da-3f4d-426c-9f76-7f46309f6fe2" />
    
19. Create Surgery with dentist role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/b20611e9-09a3-4a9e-b847-d805dfb3cdfa" />

20. Create Surgery with member role (403)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/27ed4340-e81f-4053-9ae6-8c597bfbbb55" />

22. Create Surgery with admin role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/1613b605-b615-4f0b-858d-f394a0a17f80" />

    (Table)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/ed3ec4c4-1529-464f-84dd-6e0356b13c83" />

    (Already Exist)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/af8504d4-643d-463b-8b7b-bdcdf9b9ab14" />

24. Create Appointment with dentist role (403)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/67ae6a03-dad0-416d-b3a6-915d2db8b192" />
    
26. Create Appointment with member role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/cc26088f-4688-4a16-ae87-8910610c822c" />

    (Table)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/df23760c-f12d-443b-a32a-6dc5f2f53a80" />
    
28. Create Appointment with admin role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/65f4613b-8d72-4874-b652-cf5a5054a7f4" />
    
    (Table)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/5ab0edd3-44c4-4696-bcc6-08cbde2b8f10" />

29. Duplication Appointment Error handling
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/34d5be0b-b4aa-42ab-9ae8-f665938a0c0b" />

31. More than 5 Appointment Error handling
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/1f707e89-cf48-4e5f-983a-97d21fa8f972" />

33. Past appointment date error handling
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/ee0da480-d70e-471e-a1b3-affea38ddf03" />

35. Date format error handling
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/3adc92c5-8afc-4eb9-9a58-37801a929763" />

37. Surgery not found error
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/8971b572-f48e-47b4-87cb-6f67fc2abdb9" />

38. Search by patient_id with dentist role (403)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/fbf00934-f77d-46b3-917e-101c28838f37" />

40. Search by patient_id with member role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/493e35d8-adb9-4f41-b741-ace3396e9707" />

41. Search by patient_id with admin role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/02f59818-f3e5-4374-bdb1-b79b7d921d22" />

42. Search by dentist_id with dentist role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/eba789d5-73fc-47a5-b0e7-791c9b1058da" />

44. Search by dentist_id with member role (403)
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/4b87ae21-3795-427e-8423-367daf66124b" />

45. Search by dentist_id with admin role
    <img width="1440" alt="image" src="https://github.com/user-attachments/assets/08e44739-9127-496c-8b64-9588c9ed28d6" />
    

