package com.kobylynskyi.graphql.codegen.scala;

import com.kobylynskyi.graphql.codegen.TestUtils;
import com.kobylynskyi.graphql.codegen.model.GeneratedLanguage;
import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static com.kobylynskyi.graphql.codegen.TestUtils.assertSameTrimmedContent;
import static com.kobylynskyi.graphql.codegen.TestUtils.getFileByName;
import static java.util.Collections.singletonList;

class GraphQLCodegenOpenclassesTest {

    private final File outputBuildDir = new File("build/generated");
    private final File outputScalaClassesDir = new File("build/generated/com/github/graphql");
    private final MappingConfig mappingConfig = new MappingConfig();

    @BeforeEach
    void init() {
        mappingConfig.setGenerateParameterizedFieldsResolvers(false);
        mappingConfig.setPackageName("com.github.graphql");
        mappingConfig.setGeneratedLanguage(GeneratedLanguage.SCALA);
        mappingConfig.setGenerateToString(true);
        mappingConfig.setGenerateApis(true);
        mappingConfig.setGenerateClient(true);
        mappingConfig.setGenerateEqualsAndHashCode(true);
        mappingConfig.setGenerateModelOpenClasses(true);
        mappingConfig.setGenerateBuilder(true);
    }

    @AfterEach
    void cleanup() {
        Utils.deleteDir(outputBuildDir);
    }

    @Test
    void generate_MultipleInterfacesPerType() throws Exception {
        new ScalaGraphQLCodegen(singletonList("src/test/resources/schemas/github.graphqls"),
                outputBuildDir, mappingConfig, TestUtils.getStaticGeneratedInfo()).generate();
        File[] files = Objects.requireNonNull(outputScalaClassesDir.listFiles());

        assertSameTrimmedContent(
                new File("src/test/resources/expected-classes/scala/Commit_no_final_class.scala.txt"),
                getFileByName(files, "Commit.scala"));
    }

    @Test
    void generate_MultipleInterfacesPerTypeVarFields() throws Exception {
        mappingConfig.setGenerateImmutableModels(false);
        new ScalaGraphQLCodegen(singletonList("src/test/resources/schemas/github.graphqls"),
                outputBuildDir, mappingConfig, TestUtils.getStaticGeneratedInfo()).generate();
        File[] files = Objects.requireNonNull(outputScalaClassesDir.listFiles());

        assertSameTrimmedContent(
                new File("src/test/resources/expected-classes/scala/Commit_normal_class_var_fields.scala.txt"),
                getFileByName(files, "Commit.scala"));
    }
}