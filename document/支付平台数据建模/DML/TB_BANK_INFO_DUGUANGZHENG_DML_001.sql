--银行信息同步--
INSERT INTO HSPAY.TB_BANK_INFO
  SELECT A.ID AS BANK_ID, A.NAME AS BANK_NAME,
         RTRIM(LTRIM(A.BANK_RELA, '['), ']') AS BANK_VALUE,
         REPLACE(B.BANK_LOGO, 'bank', 'bank/pc') AS BANK_LOGO_PC,
         REPLACE(REPLACE(A.IMG_URL, 'themes', 'upload'),
                  'bank',
                  'bank/mobile') AS BANK_LOGO_MOB,
         DECODE(A.FLAG, 1, 1, 2) AS QUICK_FLAG,
         DECODE(B.IS_ENABLE, 1, 1, 2) AS IS_ENABLE_PC, 1 AS IS_ENABLE_MOB
    FROM HSPRD.TB_BANK_58 A
    LEFT JOIN HSPRD.TB_ONLINE_BANK_034 B
      ON INSTR(A.NAME, B.BANK_NAME) != 0
      OR INSTR(B.BANK_NAME, A.NAME) != 0;
