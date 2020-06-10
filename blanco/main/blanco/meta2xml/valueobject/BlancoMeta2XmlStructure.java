/*
 * blanco Framework
 * Copyright (C) 2004-2009 IGA Tosiki
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.meta2xml.valueobject;

/**
 * BlancoMeta2Xml のなかで利用されるValueObjectです。
 */
public class BlancoMeta2XmlStructure {
    /**
     * フィールド名を指定します。必須項目です。
     *
     * フィールド: [name]。
     */
    private String fName;

    /**
     * パッケージ名を指定します。必須項目です。
     *
     * フィールド: [package]。
     */
    private String fPackage;

    /**
     * このバリューオブジェクトの説明を記載します。
     *
     * フィールド: [description]。
     */
    private String fDescription;

    /**
     * ファイルコメントを指定します。
     *
     * フィールド: [fileDescription]。
     * デフォルト: ["このクラスはblancoValueObjectにより自動生成されました。"]。
     */
    private String fFileDescription = "このクラスはblancoValueObjectにより自動生成されました。";

    /**
     * 変換定義ファイルの位置を指定します。
     *
     * フィールド: [convertDefFile]。
     */
    private String fConvertDefFile;

    /**
     * 入力ファイル拡張子。メタディレクトリ処理に利用されます。
     *
     * フィールド: [inputFileExt]。
     * デフォルト: [".xls"]。
     */
    private String fInputFileExt = ".xls";

    /**
     * 出力ファイル拡張子。メタディレクトリ処理に利用されます。
     *
     * フィールド: [outputFileExt]。
     * デフォルト: [".xml"]。
     */
    private String fOutputFileExt = ".xml";

    /**
     * 入力ファイル拡張子。メタディレクトリ処理に利用されます。
     *
     * フィールド: [inputFileExtSub]。
     * デフォルト: [".xlsx"]。
     */
    private String fInputFileExtSub = ".xlsx";

    /**
     * メタディレクトリ内で処理をスキップすべきファイル。Excelの一時ファイルの正規表現
     *
     * フィールド: [excludedFileRegex]。
     * デフォルト: ["~$"]。
     */
    private String fExcludedFileRegex = "~$";

    /**
     * フィールド [name] の値を設定します。
     *
     * フィールドの説明: [フィールド名を指定します。必須項目です。]。
     *
     * @param argName フィールド[name]に設定する値。
     */
    public void setName(final String argName) {
        fName = argName;
    }

    /**
     * フィールド [name] の値を取得します。
     *
     * フィールドの説明: [フィールド名を指定します。必須項目です。]。
     *
     * @return フィールド[name]から取得した値。
     */
    public String getName() {
        return fName;
    }

    /**
     * フィールド [package] の値を設定します。
     *
     * フィールドの説明: [パッケージ名を指定します。必須項目です。]。
     *
     * @param argPackage フィールド[package]に設定する値。
     */
    public void setPackage(final String argPackage) {
        fPackage = argPackage;
    }

    /**
     * フィールド [package] の値を取得します。
     *
     * フィールドの説明: [パッケージ名を指定します。必須項目です。]。
     *
     * @return フィールド[package]から取得した値。
     */
    public String getPackage() {
        return fPackage;
    }

    /**
     * フィールド [description] の値を設定します。
     *
     * フィールドの説明: [このバリューオブジェクトの説明を記載します。]。
     *
     * @param argDescription フィールド[description]に設定する値。
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * フィールド [description] の値を取得します。
     *
     * フィールドの説明: [このバリューオブジェクトの説明を記載します。]。
     *
     * @return フィールド[description]から取得した値。
     */
    public String getDescription() {
        return fDescription;
    }

    /**
     * フィールド [fileDescription] の値を設定します。
     *
     * フィールドの説明: [ファイルコメントを指定します。]。
     *
     * @param argFileDescription フィールド[fileDescription]に設定する値。
     */
    public void setFileDescription(final String argFileDescription) {
        fFileDescription = argFileDescription;
    }

    /**
     * フィールド [fileDescription] の値を取得します。
     *
     * フィールドの説明: [ファイルコメントを指定します。]。
     * デフォルト: ["このクラスはblancoValueObjectにより自動生成されました。"]。
     *
     * @return フィールド[fileDescription]から取得した値。
     */
    public String getFileDescription() {
        return fFileDescription;
    }

    /**
     * フィールド [convertDefFile] の値を設定します。
     *
     * フィールドの説明: [変換定義ファイルの位置を指定します。]。
     *
     * @param argConvertDefFile フィールド[convertDefFile]に設定する値。
     */
    public void setConvertDefFile(final String argConvertDefFile) {
        fConvertDefFile = argConvertDefFile;
    }

    /**
     * フィールド [convertDefFile] の値を取得します。
     *
     * フィールドの説明: [変換定義ファイルの位置を指定します。]。
     *
     * @return フィールド[convertDefFile]から取得した値。
     */
    public String getConvertDefFile() {
        return fConvertDefFile;
    }

    /**
     * フィールド [inputFileExt] の値を設定します。
     *
     * フィールドの説明: [入力ファイル拡張子。メタディレクトリ処理に利用されます。]。
     *
     * @param argInputFileExt フィールド[inputFileExt]に設定する値。
     */
    public void setInputFileExt(final String argInputFileExt) {
        fInputFileExt = argInputFileExt;
    }

    /**
     * フィールド [inputFileExt] の値を取得します。
     *
     * フィールドの説明: [入力ファイル拡張子。メタディレクトリ処理に利用されます。]。
     * デフォルト: [".xls"]。
     *
     * @return フィールド[inputFileExt]から取得した値。
     */
    public String getInputFileExt() {
        return fInputFileExt;
    }

    /**
     * フィールド [outputFileExt] の値を設定します。
     *
     * フィールドの説明: [出力ファイル拡張子。メタディレクトリ処理に利用されます。]。
     *
     * @param argOutputFileExt フィールド[outputFileExt]に設定する値。
     */
    public void setOutputFileExt(final String argOutputFileExt) {
        fOutputFileExt = argOutputFileExt;
    }

    /**
     * フィールド [outputFileExt] の値を取得します。
     *
     * フィールドの説明: [出力ファイル拡張子。メタディレクトリ処理に利用されます。]。
     * デフォルト: [".xml"]。
     *
     * @return フィールド[outputFileExt]から取得した値。
     */
    public String getOutputFileExt() {
        return fOutputFileExt;
    }

    /**
     * フィールド [inputFileExtSub] の値を設定します。
     *
     * フィールドの説明: [入力ファイル拡張子。メタディレクトリ処理に利用されます。]。
     *
     * @param argInputFileExtSub フィールド[inputFileExtSub]に設定する値。
     */
    public void setInputFileExtSub(final String argInputFileExtSub) {
        fInputFileExtSub = argInputFileExtSub;
    }

    /**
     * フィールド [inputFileExtSub] の値を取得します。
     *
     * フィールドの説明: [入力ファイル拡張子。メタディレクトリ処理に利用されます。]。
     * デフォルト: [".xlsx"]。
     *
     * @return フィールド[inputFileExtSub]から取得した値。
     */
    public String getInputFileExtSub() {
        return fInputFileExtSub;
    }

    /**
     * フィールド [excludedFileRegex] の値を設定します。
     *
     * フィールドの説明: [メタディレクトリ内で処理をスキップすべきファイル。Excelの一時ファイルの正規表現]。
     *
     * @param argExcludedFileRegex フィールド[excludedFileRegex]に設定する値。
     */
    public void setExcludedFileRegex(final String argExcludedFileRegex) {
        fExcludedFileRegex = argExcludedFileRegex;
    }

    /**
     * フィールド [excludedFileRegex] の値を取得します。
     *
     * フィールドの説明: [メタディレクトリ内で処理をスキップすべきファイル。Excelの一時ファイルの正規表現]。
     * デフォルト: ["~$"]。
     *
     * @return フィールド[excludedFileRegex]から取得した値。
     */
    public String getExcludedFileRegex() {
        return fExcludedFileRegex;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。
     *
     * <P>使用上の注意</P>
     * <UL>
     * <LI>オブジェクトのシャロー範囲のみ文字列化の処理対象となります。
     * <LI>オブジェクトが循環参照している場合には、このメソッドは使わないでください。
     * </UL>
     *
     * @return バリューオブジェクトの文字列表現。
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("blanco.meta2xml.valueobject.BlancoMeta2XmlStructure[");
        buf.append("name=" + fName);
        buf.append(",package=" + fPackage);
        buf.append(",description=" + fDescription);
        buf.append(",fileDescription=" + fFileDescription);
        buf.append(",convertDefFile=" + fConvertDefFile);
        buf.append(",inputFileExt=" + fInputFileExt);
        buf.append(",outputFileExt=" + fOutputFileExt);
        buf.append(",inputFileExtSub=" + fInputFileExtSub);
        buf.append(",excludedFileRegex=" + fExcludedFileRegex);
        buf.append("]");
        return buf.toString();
    }

    /**
     * このバリューオブジェクトを指定のターゲットに複写します。
     *
     * <P>使用上の注意</P>
     * <UL>
     * <LI>オブジェクトのシャロー範囲のみ複写処理対象となります。
     * <LI>オブジェクトが循環参照している場合には、このメソッドは使わないでください。
     * </UL>
     *
     * @param target target value object.
     */
    public void copyTo(final BlancoMeta2XmlStructure target) {
        if (target == null) {
            throw new IllegalArgumentException("Bug: BlancoMeta2XmlStructure#copyTo(target): argument 'target' is null");
        }

        // No needs to copy parent class.

        // Name: fName
        // Type: java.lang.String
        target.fName = this.fName;
        // Name: fPackage
        // Type: java.lang.String
        target.fPackage = this.fPackage;
        // Name: fDescription
        // Type: java.lang.String
        target.fDescription = this.fDescription;
        // Name: fFileDescription
        // Type: java.lang.String
        target.fFileDescription = this.fFileDescription;
        // Name: fConvertDefFile
        // Type: java.lang.String
        target.fConvertDefFile = this.fConvertDefFile;
        // Name: fInputFileExt
        // Type: java.lang.String
        target.fInputFileExt = this.fInputFileExt;
        // Name: fOutputFileExt
        // Type: java.lang.String
        target.fOutputFileExt = this.fOutputFileExt;
        // Name: fInputFileExtSub
        // Type: java.lang.String
        target.fInputFileExtSub = this.fInputFileExtSub;
        // Name: fExcludedFileRegex
        // Type: java.lang.String
        target.fExcludedFileRegex = this.fExcludedFileRegex;
    }
}
