package denisferreira.iso8583;

import denisferreira.iso8583.exception.FieldFormatException;
import denisferreira.iso8583.exception.FieldKeyNotFoundException;
import denisferreira.iso8583.exception.ISOMessageNotCorrectFormatException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Iso8583 {

    protected Map<Integer, Field> fieldsCfg;
    private Map<Integer, String> fields = new HashMap<>();

    public Map<Integer, Field> getFieldsCfg() {
        return fieldsCfg;
    }

    public void setFieldsCfg(Map<Integer, Field> configuracoesCampos) {
        this.fieldsCfg = configuracoesCampos;
    }

    protected String mti;
    private Logger logger = Logger.getLogger("ISO8583");

    private void configureFields() {
        fieldsCfg = new HashMap<>();
        fieldsCfg.put(1, new Field(1, Field.FIXED | Field.BINARY, 64, "Secondary bitmap"));
        this.setField(1, "");
        fieldsCfg.put(2, new Field(2, Field.LLVAR | Field.NUMERIC, 19, "Primary account number (PAN)"));
        fieldsCfg.put(3, new Field(3, Field.FIXED | Field.NUMERIC, 6, "Processing code"));
        fieldsCfg.put(4, new Field(4, Field.FIXED | Field.NUMERIC, 12, "Amount, transaction"));
        fieldsCfg.put(5, new Field(5, Field.FIXED | Field.NUMERIC, 12, "Amount, settlement"));
        fieldsCfg.put(6, new Field(6, Field.FIXED | Field.NUMERIC, 12, "Amount, cardholder billing"));
        fieldsCfg.put(7, new Field(7, Field.FIXED | Field.NUMERIC, 10, "Transmission date & time"));
        fieldsCfg.put(8, new Field(8, Field.FIXED | Field.NUMERIC, 8, "Amount, cardholder billing fee"));
        fieldsCfg.put(9, new Field(9, Field.FIXED | Field.NUMERIC, 8, "Conversion rate, settlement"));
        fieldsCfg.put(10, new Field(10, Field.FIXED | Field.NUMERIC, 8, "Conversion rate, cardholder billing"));
        fieldsCfg.put(11, new Field(11, Field.FIXED | Field.NUMERIC, 6, "System trace audit number (STAN)"));
        fieldsCfg.put(12, new Field(12, Field.FIXED | Field.NUMERIC, 6, "Local transaction time (hhmmss)"));
        fieldsCfg.put(13, new Field(13, Field.FIXED | Field.NUMERIC, 4, "Local transaction date (MMDD)"));
        fieldsCfg.put(14, new Field(14, Field.FIXED | Field.NUMERIC, 4, "Expiration date"));
        fieldsCfg.put(15, new Field(15, Field.FIXED | Field.NUMERIC, 4, "Settlement date"));
        fieldsCfg.put(16, new Field(16, Field.FIXED | Field.NUMERIC, 4, "Currency conversion date"));
        fieldsCfg.put(17, new Field(17, Field.FIXED | Field.NUMERIC, 4, "Capture date"));
        fieldsCfg.put(18, new Field(18, Field.FIXED | Field.NUMERIC, 4, "Merchant type, or merchant category code"));
        fieldsCfg.put(19, new Field(19, Field.FIXED | Field.NUMERIC, 4, "Acquiring institution (country code)"));
        fieldsCfg.put(20, new Field(20, Field.FIXED | Field.NUMERIC, 3, "PAN extended (country code)"));
        fieldsCfg.put(21, new Field(21, Field.FIXED | Field.NUMERIC, 3, "Forwarding institution (country code)"));
        fieldsCfg.put(22, new Field(22, Field.FIXED | Field.NUMERIC, 3, "Point of service entry mode"));
        fieldsCfg.put(23, new Field(23, Field.FIXED | Field.NUMERIC, 3, "Application PAN sequence number"));
        fieldsCfg.put(25, new Field(24, Field.FIXED | Field.NUMERIC, 3, "Function code (ISO 8583:1993), or network international identifier (NII)"));
        fieldsCfg.put(26, new Field(24, Field.FIXED | Field.NUMERIC, 3, "Function code (ISO 8583:1993), or network international identifier (NII)"));
        fieldsCfg.put(27, new Field(24, Field.FIXED | Field.NUMERIC, 3, "Function code (ISO 8583:1993), or network international identifier (NII)"));
        fieldsCfg.put(28, new Field(24, Field.FIXED | Field.NUMERIC, 3, "Function code (ISO 8583:1993), or network international identifier (NII)"));
        fieldsCfg.put(29, new Field(24, Field.FIXED | Field.NUMERIC, 3, "Function code (ISO 8583:1993), or network international identifier (NII)"));
        fieldsCfg.put(30, new Field(24, Field.FIXED | Field.NUMERIC, 3, "Function code (ISO 8583:1993), or network international identifier (NII)"));
        fieldsCfg.put(31, new Field(24, Field.FIXED | Field.NUMERIC, 3, "Function code (ISO 8583:1993), or network international identifier (NII)"));
        fieldsCfg.put(32, new Field(32, Field.LLVAR | Field.NUMERIC, 15, "Código do estabelecimento Gateway"));
        fieldsCfg.put(35, new Field(35, Field.LLVAR | Field.Z_STRING, 37, "Trilha 2 do cartao"));
        fieldsCfg.put(38, new Field(38, Field.FIXED | Field.NUMERIC | Field.ALPHA, 6, "Numero da Autorizacao"));
        fieldsCfg.put(39, new Field(39, Field.FIXED | Field.NUMERIC | Field.ALPHA, 2, "Codigo de Resposta"));
        fieldsCfg.put(40,
                new Field(40, Field.FIXED | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 3, "Versao da Mensagem"));
        fieldsCfg.put(41, new Field(41, Field.FIXED | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 8,
                "Identificacao do Terminal (Check-out)"));
        fieldsCfg.put(42, new Field(42, Field.FIXED | Field.NUMERIC, 15, "Codigo do EC"));
        fieldsCfg.put(43, new Field(43, Field.FIXED | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 40,
                "Numero de serie do PINPad"));
        fieldsCfg.put(44, new Field(44, Field.LLVAR | Field.ALPHA, 2, "Dados adicionais da resposta"));
        fieldsCfg.put(45, new Field(45, Field.LLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 76, "Track 1 Data"));
        fieldsCfg.put(48, new Field(48, Field.LLLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 999,
                "Dados adicionais da transacao - ADIQ"));
        fieldsCfg.put(49, new Field(49, Field.FIXED | Field.ALPHA, 3, "Codigo da Moeda Corrente"));
        fieldsCfg.put(52, new Field(52, Field.FIXED | Field.BINARY, 16, "Personal Identification Number (PIN)"));
        fieldsCfg.put(54, new Field(54, Field.LLLVAR | Field.ALPHA | Field.NUMERIC | Field.SPECIAL, 999,
                "Transação da carga do setup inicial da rede"));
        fieldsCfg.put(55, new Field(55, Field.LLLVAR | Field.BINARY, 999, "Dados Relativos ao ICC (Smart Card)"));
        fieldsCfg.put(56, new Field(56, Field.LLLVAR | Field.NUMERIC, 999, "Dados Relativos ao CHIP (Tags EMV)"));
        fieldsCfg.put(57, new Field(57, Field.LLLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 13,
                "Data + NSU da ultima transacao OK"));
        fieldsCfg.put(58, new Field(58, Field.LLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 6,
                "Identificacao do Terminal TEF Virtualizado"));
        fieldsCfg.put(59,
                new Field(59, Field.LLLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 999, "Dados Adicionais"));
        fieldsCfg.put(60, new Field(60, Field.LLLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 999,
                "Dados Adicionais da transação ADIQ"));
        fieldsCfg.put(61, new Field(61, Field.LLLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 999,
                "Dados do Ponto de Venda - ADIQ"));
        fieldsCfg.put(62, new Field(62, Field.LLLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 999,
                "Dados Adicionais Para Transacoes Financeiras"));
        fieldsCfg.put(63, new Field(63, Field.LLLVAR | Field.NUMERIC | Field.ALPHA | Field.SPECIAL, 999,
                "Dados de Carga de Tabela"));
        fieldsCfg.put(66,
                new Field(66, Field.LLLVAR | Field.NUMERIC | Field.ALPHA, 999, "Número de série do terminal"));
        fieldsCfg.put(67, new Field(67, Field.FIXED | Field.NUMERIC, 2, "Numero de Parcelas"));
        fieldsCfg.put(70,
                new Field(70, Field.FIXED | Field.NUMERIC, 3, "Código de Gerrenciamento de Rede. Fixo '001'"));
        fieldsCfg.put(71, new Field(71, Field.FIXED | Field.NUMERIC, 8,
                "Indicacao da necessidade de atualizacao da Tabela - Gateway"));
        fieldsCfg.put(72, new Field(72, Field.FIXED | Field.NUMERIC, 4, "Último número de mensagem"));
        fieldsCfg.put(73, new Field(73, Field.FIXED | Field.NUMERIC, 6, "Último número de mensagem"));
        fieldsCfg.put(74, new Field(74, Field.FIXED | Field.NUMERIC, 10));
        fieldsCfg.put(75, new Field(75, Field.FIXED | Field.NUMERIC, 10));
        fieldsCfg.put(76, new Field(76, Field.FIXED | Field.NUMERIC, 10));
        fieldsCfg.put(77, new Field(77, Field.FIXED | Field.NUMERIC, 10));
        fieldsCfg.put(78, new Field(78, Field.FIXED | Field.NUMERIC, 10));
        fieldsCfg.put(79, new Field(79, Field.FIXED | Field.NUMERIC, 10));
        fieldsCfg.put(80, new Field(80, Field.FIXED | Field.NUMERIC, 10));
        fieldsCfg.put(81, new Field(81, Field.FIXED | Field.NUMERIC, 10));
        fieldsCfg.put(82, new Field(82, Field.FIXED | Field.NUMERIC, 12));
        fieldsCfg.put(83, new Field(83, Field.FIXED | Field.NUMERIC, 12));
        fieldsCfg.put(84, new Field(84, Field.FIXED | Field.NUMERIC, 12));
        fieldsCfg.put(85, new Field(85, Field.FIXED | Field.NUMERIC, 12));
        fieldsCfg.put(86, new Field(86, Field.FIXED | Field.NUMERIC, 16));
        fieldsCfg.put(87, new Field(87, Field.FIXED | Field.NUMERIC, 16));
        fieldsCfg.put(88, new Field(88, Field.FIXED | Field.NUMERIC, 16));
        fieldsCfg.put(89, new Field(89, Field.FIXED | Field.NUMERIC, 16));
        fieldsCfg.put(90, new Field(90, Field.FIXED | Field.Z_STRING, 42, "Dados da Transacao Original"));
        fieldsCfg.put(99, new Field(99, Field.LLVAR | Field.ALPHA | Field.NUMERIC | Field.SPECIAL, 99,
                "Identificacao Estabelecimento - Terminal ADIQ"));
        fieldsCfg.put(100, new Field(100, Field.LLVAR | Field.ALPHA | Field.NUMERIC, 11,
                "Codigo de rede de captura. Fixo 'WTNET'"));
        fieldsCfg.put(104, new Field(104, Field.LLLVAR | Field.ALPHA | Field.NUMERIC | Field.SPECIAL, 100,
                "Descrição da Transação"));
        fieldsCfg.put(116, new Field(116, Field.LLLVAR | Field.ALPHA | Field.NUMERIC | Field.SPECIAL, 999,
                "Indicacao de telecarga (Atualizacao de software)"));
        fieldsCfg.put(126,
                new Field(126, Field.LLLVAR | Field.ALPHA | Field.NUMERIC | Field.SPECIAL, 999, "Dados de Segurança"));
        fieldsCfg.put(127, new Field(127, Field.LLLVAR | Field.NUMERIC | Field.ALPHA, 12, "NSU do Host ADIQ"));
        fieldsCfg.put(128,
                new Field(128, Field.LLLVAR | Field.NUMERIC | Field.ALPHA, 999, "Código de autenticação da mensagem"));

    }

    public Iso8583() {
        if (fieldsCfg == null) {
            configureFields();
        }
    }

    public String serialize() {
        StringBuffer output = new StringBuffer();
        logger.log(Level.INFO, "MTI: " + getMti());
        List<Integer> bits = new Vector<>();
        for (int i = 1; i < 128; i++)
            if (getFields().containsKey(i))
                bits.add(i);

        byte[] map = new byte[bits.get(0) == 1 ? 16 : 8];
        byte tempMap, r;
        int q;

        for (Integer bit : bits) {
            q = bit / 8;
            r = Byte.parseByte(String.valueOf(bit % 8));

            tempMap = 0x01;
            if (r != 0)
                tempMap = (byte) (tempMap << (8 - r));
            else
                q--;

            map[q] = (byte) (map[q] | tempMap);
        }

        output.append(this.getMti());
        output.append(BCD.BcdToString(map));
        for (Integer bit : bits) {
            Field myField = fieldsCfg.get(bit);

            if (myField == null) {
                logger.log(Level.SEVERE, "Bit " + bit + ": not found bit!");
                throw new FieldKeyNotFoundException(bit);
            }

            if (myField.serializeField(getField(bit), output) == -1) {
                logger.log(Level.SEVERE, "Bit " + bit + ": badly formatted bit");
                throw new FieldFormatException(bit, myField.getFormat(), getField(bit));
            }
            logger.log(Level.INFO, "Req Bit:" + ((bit < 10) ? (" " + bit) : bit) + " = " + getField(bit) + " # "
                    + myField.getDescription());
        }
        return output.toString();
    }

    public void unserialize(String iso) throws ISOMessageNotCorrectFormatException {
        try {
            int lenMap = (iso.charAt(4) > '7' ? 16 : 8), offset = 4 + (lenMap * 2);
            byte[] bitmap;

            this.setMti(iso.substring(0, 4));

            bitmap = BCD.StringToBcd(iso.substring(4, offset));

            Vector<Integer> bits = new Vector<>();
            for (int i = 0; i < lenMap; i++) {
                if ((bitmap[i] & 0x80) == 0x80)
                    bits.add(i * 8 + 1);
                if ((bitmap[i] & 0x40) == 0x40)
                    bits.add(i * 8 + 2);
                if ((bitmap[i] & 0x20) == 0x20)
                    bits.add(i * 8 + 3);
                if ((bitmap[i] & 0x10) == 0x10)
                    bits.add(i * 8 + 4);
                if ((bitmap[i] & 0x08) == 0x08)
                    bits.add(i * 8 + 5);
                if ((bitmap[i] & 0x04) == 0x04)
                    bits.add(i * 8 + 6);
                if ((bitmap[i] & 0x02) == 0x02)
                    bits.add(i * 8 + 7);
                if ((bitmap[i] & 0x01) == 0x01)
                    bits.add(i * 8 + 8);
            }
            logger.log(Level.INFO, "MTI: " + getMti());
            String value;
            for (Integer bit : bits) {

                Field myField = fieldsCfg.get(bit);
                value = myField.unserializeField(iso, offset);

                logger.log(Level.INFO, "Res Bit:" + ((bit < 10) ? (" " + bit) : bit) + " = " + value + " # "
                        + myField.getDescription());

                setField(bit, value);
                offset += value.length();
                if ((fieldsCfg.get(bit).getFormat() & Field.LLLVAR) == Field.LLLVAR)
                    offset += 3;
                else if ((fieldsCfg.get(bit).getFormat() & Field.LLVAR) == Field.LLVAR)
                    offset += 2;
            }
        } catch (Exception e) {
            throw new ISOMessageNotCorrectFormatException(e.getMessage());
        }
    }

    protected void setFieldSize(Integer key, Integer size) {
        if (fieldsCfg.containsKey(key)) {
            fieldsCfg.get(key).setMaxLen(size);
        } else
            throw new FieldKeyNotFoundException(key);
    }

    public String getField(Integer key) {
        return getFields().get(key);
    }

    public boolean hasField(Integer key) {
        return getFields().containsKey(key);
    }

    protected void setField(Integer key, String value) {
        getFields().put(key, value);
    }

    protected void unsetField(Integer key) {
        getFields().remove(key);
    }

    protected void unsetFields(int[] keys) {
        for (int x : keys) {
            if (fieldsCfg.containsKey(x))
                unsetField(x);
        }
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public Map<Integer, String> getFields() {
        return fields;
    }

    public void setFields(Map<Integer, String> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "denisferreira.iso8583 [fields=" + getFields().toString() + ", mti=" + mti + "]";
    }


}
