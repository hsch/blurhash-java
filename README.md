# BlurHash for Java

## About

`io.trbl:blurhash` is a [BlurHash](https://blurha.sh) encoder implementation in Java.

It's mostly based on the TypeScript implementation,
with some influences from the Python implementation.

## How to use it

Example usage:

```java
BufferedImage image = ImageIO.read(new File("lorikeet.jpg"));
String blurHash = BlurHash.encode(image); // UFDcT@_LNs#pVrIVX6Vu9]RRw[OXOZxaxWNH
```

Gradle dependency:

```
dependencies {
    implementation "io.trbl:blurhash:1.0.0"
}
```

Maven dependency:
```
<dependency>
    <groupId>io.trbl</groupId>
    <artifactId>blurhash</artifactId>
    <version>1.0.0</version>
</dependency>
```

## How to build it

```
./gradlew build
```

## How to release it

Most information is available here:
- https://central.sonatype.org/pages/ossrh-guide.html
- https://central.sonatype.org/pages/gradle.html
- https://central.sonatype.org/pages/working-with-pgp-signatures.html

Some additional tips and tricks might be needed.

Note that with recent GPG versions,
you might need to export the secret keyring to a local file:
```
gpg --export-secret-keys > secring.gpg
```

Find your key ID:
```
gpg --list-secret-keys --keyid-format 0xSHORT
```

Dry-run the signing process:
```
./gradlew signArchives
```

Make sure your key is available on a public server:
```
gpg --keyserver keyserver.ubuntu.com --send-keys ...
```

Finally, upload the package to staging like so:
```
./gradlew clean uploadArchives
```

Manage staging repositories here:
- https://oss.sonatype.org
- https://issues.sonatype.org/

Remember that releasing the package is done by closing the staging repository.

## MIT License

Copyright (c) 2019 Hendrik Schnepel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
