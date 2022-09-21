package ca.ulaval.glo4002.part1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ImageEditorTest {
    private static final String AN_IMAGE = "potato";
    private static final String EXPECTED_PATH = "/tmp/part1-images/potato.png";

    private ImageEditor imageEditor;

    @Test
    void whenLoadingAnImage_thenLoadsImageFromTheRightDirectory() {
        ImageLoaderDouble imageLoader = new ImageLoaderDouble();
        imageEditor = new ImageEditor(imageLoader);

        imageEditor.renderImageByName(AN_IMAGE);

        Assertions.assertTrue(imageLoader.wasLoadCalled);
        Assertions.assertEquals(EXPECTED_PATH, imageLoader.loadCallParameter);
    }

    @Test
    void whenLoadingAnImage_thenLoadsImageFromTheRightDirectory_withMockito() {
        ResourcesImageLoader resourcesImageLoader = Mockito.mock(ResourcesImageLoader.class);
        imageEditor = new ImageEditor(resourcesImageLoader);

        imageEditor.renderImageByName(AN_IMAGE);

        Mockito.verify(resourcesImageLoader).load(EXPECTED_PATH);
    }

    private static class ImageLoaderDouble implements ImageLoader {

        public boolean wasLoadCalled = false;
        public String loadCallParameter = "";

        @Override
        public byte[] load(String path) {
            wasLoadCalled = true;
            loadCallParameter = path;

            return new byte[]{};
        }
    }
}
