const requireAll = (requireContext: any) => requireContext.keys().map(requireContext)
const req = require.context('@/assets/images', false, /\.svg$/)
requireAll(req)
