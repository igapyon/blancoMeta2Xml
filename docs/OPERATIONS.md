# blancoMeta2XmlのビルドとRelease運用

## バージョンとJava要件

本体版は`pom.xml`のproject versionを正とします。AntもPOMから読み込みます。現在の候補版は`2.2.1`、対応するタグは`v2.2.1`です。

定数定義書に残る過去の版は、生成後に`build.xml`の`stamp-version`でPOMの版へ置き換えます。MavenとAntの両方がこの処理を使用するため、本体の`BlancoMeta2XmlConstants.VERSION`も一致します。生成ツール自身が出す版の表示は、そのツールの版です。

ビルド・実行検証にはJDK 17を使用します。Mavenは3.6.3以降、Antは1.10.18を使用します。本体は`release=8`でコンパイルし、class major 52を維持します。ただし実行時依存の`google-java-format:1.15.0`にはmajor 55（Java 11形式）のclassがあり、依存全体を含めたJava 8 JVMでの動作は保証しません。

## 公開依存による生成

生成に使うBlanco系の依存は、公開repository `https://raw.githubusercontent.com/uedaueo/blancofw-maven2/gh-pages-poi5/`から取得します。直接依存だけでなく、上流POMの範囲指定による推移依存も、次の版へdependencyManagementで固定しています。

| groupId | artifactId | version |
| --- | --- | --- |
| blanco.anttask | blanco-anttask | 2.1.6 |
| blanco.batchprocess | blanco-batchprocess | 2.1.5 |
| blanco.commons.io | blanco-commons | 2.1.7 |
| blanco.cg | blanco-cg | 2.3.19 |
| blanco.valueobject | blanco-valueobject | 2.1.12 |
| blanco.constants | blanco-constants | 2.1.5 |
| blanco.meta2xml | blanco-meta2xml | 2.1.3 |
| blanco.resourcebundle | blanco-resourcebundle | 2.1.5 |
| blanco.xml.bind | blanco-xmlbinding | 2.1.5 |
| blanco.message | blanco-message | 2.1.4 |
| blanco.stringgroup | blanco-stringgroup | 0.5.4 |
| blanco.beanutils | blanco-beanutils | 1.1.1 |

`blanco.meta2xml:blanco-meta2xml:2.1.3`は自身を生成するための公開旧版です。`provided` scopeと各Blanco依存のexclusionにより、生成時だけ使用し、利用者の実行時依存へ旧版を持ち込みません。生成classpathはMavenの`initialize`で依存JARのみから作り、`target/classes`を含めません。生成時にはbootstrap taskの読込元JARをログへ表示します。ローカルにだけinstallしたartifactは前提としません。

この`blanco.cg:blanco-cg:2.3.19`と、別途整備された`jp.igapyon.blanco.cg:blanco-cg`は異なる座標です。

POI一式は`5.5.1`、XMLBeansは`5.3.0`を使用します。旧`poi-ooxml-schemas`は除外します。AntRun pluginのCommons Netは`3.13.0`へ固定しています。通常依存とplugin依存は別に確認してください。

## Mavenによるビルドと検証

```sh
mvn --batch-mode --no-transfer-progress clean verify
```

`meta/program/`の定義書から`blanco/main/`を生成してコンパイルします。既存の生成物がなくても実行できます。

Gitでは定義書と生成処理を管理し、`blanco/main/`全体を管理外とします。`/blanco/main`のignoreを維持し、生成物を強制追加しません。生成結果を変更する場合は、元の定義書または生成処理を修正して再生成します。`git ls-files -- blanco/main`で追跡済みファイルがないことを確認できます。

出力は`target/blanco-meta2xml-<version>.jar`と`target/blanco-meta2xml-<version>-sources.jar`です。通常JARには`blanco/meta2xml/BlancoMeta2XmlMeta2Xml.xml`と、生成されたproperties・XMLがあればそれらも含めます。sources JARには通常ソースと生成済みソースを含めます。source pluginは`jar-no-fork`を使用し、sources JAR作成のために生成フェーズを再実行しません。

`verify`ではFailsafeが完成JARを対象に5件の統合テストを実行します。隔離classloaderには完成JARとruntime依存だけを渡し、`target/classes`と生成用旧版JARに頼らず確認します。

- XMLの収録元、本体版、全本体classのmajor 52、通常・生成済みソースの収録
- XLSXの定義書からXML・Javaを生成し、Java 8形式でコンパイルして再実行
- XLS形式の定義書読込
- `meta/template/BlancoMeta2XmlTemplate.xlsx`の読込
- `BlancoMeta2XmlStructure.xlsx`からValueObjectのXML・Javaを生成してコンパイル

結果は`target/failsafe-reports/`です。`mvn test`や`mvn package`だけでは、この完成JARの検証まで終わりません。

空キャッシュから確認する場合は、未使用の一時ディレクトリを指定します。

```sh
mvn --batch-mode --no-transfer-progress -Dmaven.repo.local=/tmp/meta2xml-fresh-m2 clean verify
```

## Antによるビルド

```sh
ant build
```

Antは同じPOMの依存を`target/lib/`へ取得し、公開旧版を使って生成します。Maven起動時は既存の`MAVEN_OPTS`を保持してIPv4優先を追加します。隔離キャッシュを使う場合は`ant -Dmaven.repo.local=/tmp/meta2xml-ant-m2 build`と指定できます。

出力は`lib.ant/blancometa2xml-<version>.jar`と`blancoMeta2Xml-<version>-src.zip`です。GitHub ReleaseにはMaven版JARを使用します。ソースZIPから`.git/`、`target/`、`workplace/`、`release-assets/`、`lib.ant/`を除きます。

## CIとタグ起点のRelease

[`build.yml`](../.github/workflows/build.yml)は`master`へのpush、`master`宛てのPull Request、手動実行で、Temurin JDK 17による`clean verify`を実行します。権限は`contents: read`です。

[`release.yml`](../.github/workflows/release.yml)は`v*`タグのpushで、そのタグのソースを取得します。タグの`v`以降とPOMのproject versionを完全一致で照合し、`clean verify`の成功後に次の2ファイルをGitHub Releaseへ添付します。Releaseには`contents: write`を使用します。

- `blanco-meta2xml-<version>.jar`
- `blanco-meta2xml-sources-<version>.jar`

Actionsは参照元blancoCgと同じcommit SHAへ固定しています。設定の基準は[igapyonv3のRelease workflow](https://github.com/igapyon/igapyonv3/blob/bf9e9e509bdfee754975ded625ad320ce3cd837f/.github/workflows/release.yml)です。同名のRelease添付ファイルがあれば置き換えます。

変更をレビューしてcommit・pushし、PRを`master`へmergeした後、POMの版に対応するタグをそのmerge commitに作成してpushします。タグのpushが配布を開始します。CIとRelease workflowの成功、タグ・POM・merge commitの一致、2つの添付JARを確認してください。workflowは公開した2JARをダウンロードしてSHA-256を照合し、ダウンロードしたJARで同じ5件の統合テストを再実行します。GitHub上でこのステップまで成功したことと、Dependabotの再評価を確認します。

本POMにはGPG署名executionがないため、CIに署名回避オプションは不要です。CI・Release workflowは`deploy`を実行しません。既存のMaven配布設定（`uedaueo/blancofw-maven2`、`gh-pages-poi4`）と、依存取得先（`gh-pages-poi5`）と、今回のGitHub Releaseは別の配布経路です。既存配布先への`mvn deploy`は、権限と意図を確認して別途実行する操作です。

## ローカル作業領域と生成差分

`workplace/`はGit管理外です。ログ、検証コピー、PR下書きはここかリポジトリ外へ置きます。`git ls-files workplace`で追跡済みファイルがないことを確認してください。Ant配布物と`release-assets/`もignore対象です。

公開旧版の生成ツールには、コメントや例外文の英語化が含まれます。旧構成の生成結果との間に、日本語コメント・例外文の差が現れることがあります。生成経路の固定と`jar-no-fork`により、コンパイルした生成ソースとsources JARの内容を揃えます。

## 整備時の確認（2026-10-27）

生成物なし・それぞれ別の空キャッシュから公開依存だけを取得し、JDK 17によるMavenの`clean verify`とAnt 1.10.18の`build`が成功しました。Mavenの完成JARの5件の統合テストと、配布名へステージした2JARを指定する5件の再検証も成功しました。Maven版・Ant版とも本体9classはすべてmajor 52で、XMLリソースと通常・生成済みソースの収録、旧版JARを除いた実行環境での生成・コンパイルを確認しました。公開後のCI・Release・ダウンロード後検証は、変更のmergeとタグ公開後に確認します。
