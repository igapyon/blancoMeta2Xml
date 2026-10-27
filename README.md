# blancoMeta2Xml

## ビルド

ビルドと動作確認にはJDK 17、Maven 3.6.3以降を使用します。Antを使う場合は1.10.18を使用します。

```sh
mvn --batch-mode --no-transfer-progress clean verify
ant build
```

Mavenは定義書からの生成、コンパイル、通常JAR・sources JARの作成、完成JARを使う5件の統合テストを実行します。`package`まででは統合テストを実行しないため、確認には`verify`を使用します。Antも公開依存を解決してから生成・コンパイル・配布物作成を行います。

本体バージョンの参照元は`pom.xml`です。Maven・Antとも本体classをJava 8形式（major 52）で出力します。ただし依存にはJava 11形式のclassも含まれ、Java 8 JVMでの実行は保証していません。検証した実行環境はJDK 17です。

## GitHub Release

`master`へのpush・Pull Request・手動実行でCIを実行します。`v*`タグのpushでは、タグとPOMのバージョン一致を確認し、ビルドと統合テストの成功後に次の2ファイルをGitHub Releaseへ添付します。

- `blanco-meta2xml-<version>.jar`
- `blanco-meta2xml-sources-<version>.jar`

通常JARは依存ライブラリを含まないライブラリJARです。必要なXMLリソースを含み、sources JARには通常ソースと生成済みソースを含みます。

生成用の旧版artifact、固定依存、空キャッシュでの検証、Antの出力先、Release手順、既存Maven配布設定については[運用手順](docs/OPERATIONS.md)を参照してください。`workplace/`はGit管理外のローカル作業領域です。
